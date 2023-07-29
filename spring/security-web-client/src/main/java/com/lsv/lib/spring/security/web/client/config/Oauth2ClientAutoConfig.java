package com.lsv.lib.spring.security.web.client.config;

import com.lsv.lib.core.function.Resolver;
import com.lsv.lib.security.web.helper.oidc.IssuerHelper;
import com.lsv.lib.security.web.properties.oidc.Issuer;
import com.lsv.lib.spring.security.web.annotation.ConditionalWebSecurityEnable;
import com.lsv.lib.spring.security.web.client.resolver.ConverterOAuth2UserOidcResolver;
import com.lsv.lib.spring.security.web.client.resolver.IssuersClientsRegistrationResolver;
import com.lsv.lib.spring.security.web.config.SpringSecurityWebAutoConfig;
import com.lsv.lib.spring.security.web.extraprocess.ClientLoginExtraProcess;
import com.nimbusds.jwt.JWTParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.util.Collection;
import java.util.Map;

import static com.lsv.lib.security.web.helper.oidc.ConstantsWebOidc.PARAM_ID_TOKEN_HINT;
import static com.lsv.lib.spring.core.helper.ConstantsSpring.SPRING_APPLICATION_NAME;

/**
 * Autoconfiguration for spring-secutiry-web-client module.
 * <p>
 * https://docs.spring.io/spring-security/6.1.1/reference/servlet/oauth2/client/index.html
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
@Getter(AccessLevel.PRIVATE)

@ConditionalWebSecurityEnable
@AutoConfiguration(after = {SpringSecurityWebAutoConfig.class})
public class Oauth2ClientAutoConfig {

    /**
     * Extra process to configure the login part if there is an issuer configured that enables the client.
     */
    @Bean
    @ConditionalOnMissingBean
    public ClientLoginExtraProcess defaultClientLoginConfigOauth2(@SuppressWarnings(SPRING_APPLICATION_NAME)
                                                                      IssuerHelper issuerHelper,
                                                                  OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService,
                                                                  Converter<OAuth2LoginAuthenticationToken, OAuth2AuthenticationToken> converterOAuth2AuthResolver,
                                                                  LogoutHandler logoutHandler) {

        if (!issuerHelper.hasIssuerClients()) {
            log.trace("Não tem issuers com cliente habilitado");
            return null;
        }

        return httpSecurity -> {
            log.trace("Configurando login client oauth2");

            try {
                httpSecurity
                    .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint(userInfo -> userInfo
                            .oidcUserService(oidcUserService)
                        )
                        .addObjectPostProcessor(new ObjectPostProcessor<OAuth2LoginAuthenticationFilter>() {
                            @Override
                            public <O extends OAuth2LoginAuthenticationFilter> O postProcess(O filterLogin) {
                                filterLogin.setAuthenticationResultConverter(converterOAuth2AuthResolver);
                                return filterLogin;
                            }
                        })
                    )
                    .logout(logout -> logout
                        .addLogoutHandler(logoutHandler)
                    )
                ;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * DSL to manually create the spring object that would be generated if the spring.security.oauth2.client properties were used.
     */
    @Bean
    @ConditionalOnMissingBean
    public ClientRegistrationRepository defaultClientRegistrationRepository(@SuppressWarnings(SPRING_APPLICATION_NAME)
                                                                                IssuerHelper issuerHelper,
                                                                            Resolver<Collection<Issuer>, Map<String, ClientRegistration>> issuersClientsRegistrationResolver) {
        return issuerHelper.hasIssuerClients()
            ? new InMemoryClientRegistrationRepository(issuersClientsRegistrationResolver.resolve(issuerHelper.filterIssuerClients()))
            : registrationId -> null;
    }

    /**
     * Defines the default property mapping.
     */
    @Bean
    @ConditionalOnMissingBean
    public IssuersClientsRegistrationResolver defaultIssuersClientsRegistrationResolver() {
        return new IssuersClientsRegistrationResolver();
    }

    /**
     * Responsible for defining the data contained in the logged in user.
     * Used to load custom roles from the received jwt.
     * Used at this point because access to the userRequest is required to get the original token received.
     */
    @Bean
    @ConditionalOnMissingBean
    public OAuth2UserService<OidcUserRequest, OidcUser> defaultOidcUserService(@SuppressWarnings(SPRING_APPLICATION_NAME)
                                                                                   IssuerHelper issuerHelper
    ) {
        final var delegate = new OidcUserService();

        return (userRequest) -> {
            var oidcUser = delegate.loadUser(userRequest);

            log.trace("Configurando usuário para token: {}", userRequest.getAccessToken().getTokenValue());

            try {
                var claims = JWTParser
                    .parse(userRequest.getAccessToken().getTokenValue())
                    .getJWTClaimsSet().getClaims();

                var issuer = issuerHelper.findIssuerByClaims(claims);

                return new DefaultOidcUser(
                    issuerHelper.extractRoles(issuer, claims, SimpleGrantedAuthority::new),
                    oidcUser.getIdToken(),
                    oidcUser.getUserInfo(),
                    issuer.getUserNameAttribute()
                );
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ConverterOAuth2UserOidcResolver defaultConverterOAuth2AuthResolverByIssuer() {
        return new ConverterOAuth2UserOidcResolver();
    }

    /**
     * Default settings for logout event.
     * Sends a logout command to the OpenId server to log the user out.
     */
    @Bean
    @ConditionalOnMissingBean
    public LogoutHandler defaultLogoutHandler(@SuppressWarnings(SPRING_APPLICATION_NAME)
                                                  IssuerHelper issuerHelper) {
        var restTemplate = new RestTemplate();

        return (request, response, authentication) -> {
            var user = (OidcUser) authentication.getPrincipal();
            var issuer = issuerHelper.findIssuer(user.getIssuer().toString());

            var uri = UriComponentsBuilder
                .fromUriString(issuer.getEndPoints().getEndSessionEndpoint())
                .queryParam(PARAM_ID_TOKEN_HINT, user.getIdToken().getTokenValue())
                .toUriString();

            var logoutResponse = restTemplate.getForEntity(uri, String.class);
            if (logoutResponse.getStatusCode().is2xxSuccessful()) {
                log.trace("Logout com sucesso");
            } else {
                log.error("Erro ao tentar realizar o logout em {}", uri);
            }
        };
    }
}