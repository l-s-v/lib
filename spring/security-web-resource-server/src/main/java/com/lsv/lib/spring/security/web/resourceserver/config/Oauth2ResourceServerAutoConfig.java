package com.lsv.lib.spring.security.web.resourceserver.config;

import com.lsv.lib.core.function.Resolver;
import com.lsv.lib.security.web.helper.oidc.IssuerHelper;
import com.lsv.lib.security.web.properties.oidc.Issuer;
import com.lsv.lib.spring.security.web.annotation.ConditionalWebSecurityEnable;
import com.lsv.lib.spring.security.web.config.SpringSecurityWebAutoConfig;
import com.lsv.lib.spring.security.web.extraprocess.ResourceServerExtraProcess;
import com.lsv.lib.spring.security.web.resourceserver.resolver.*;
import com.lsv.lib.spring.security.web.resourceserver.resolver.AuthenticationManagerResolverByIssuer.AuthenticationManagerDataCache;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static com.lsv.lib.spring.core.helper.ConstantsSpring.SPRING_APPLICATION_NAME;

/**
 * Autoconfiguration for spring-secutiry-web-resource-server module.
 * <p>
 * https://docs.spring.io/spring-security/reference/6.1.1/servlet/oauth2/resource-server/jwt.html
 * https://docs.spring.io/spring-security/reference/6.1.1/servlet/oauth2/resource-server/opaque-token.html
 * https://docs.spring.io/spring-security/reference/6.1.1/servlet/oauth2/resource-server/multitenancy.html
 * https://docs.spring.io/spring-security/reference/6.1.1/servlet/oauth2/resource-server/bearer-tokens.html
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
@Getter(AccessLevel.PRIVATE)

@ConditionalWebSecurityEnable
@AutoConfiguration(after = {SpringSecurityWebAutoConfig.class})
public class Oauth2ResourceServerAutoConfig {

    /**
     * Extra process to configure the login part if there is an issuer configured that enables the client.
     */
    @Bean
    @ConditionalOnMissingBean
    public ResourceServerExtraProcess defaultResourceServerConfigOauth2(@SuppressWarnings(SPRING_APPLICATION_NAME)
                                                                            IssuerHelper issuerHelper,
                                                                        AuthenticationEntryPoint authenticationEntryPoint,
                                                                        AuthenticationManagerResolver authenticationManagerResolver) {

        if (!issuerHelper.hasIssuerResourceServers()) {
            log.trace("Não tem issuers com resource server habilitado");
            return null;
        }

        return httpSecurity -> {
            log.trace("Configurando login resource server oauth2");

            try {
                httpSecurity
                    // Following the pattern of the previous library
                    .csrf(csrf -> csrf.disable())
                    .headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
                    .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .authenticationManagerResolver(authenticationManagerResolver)
                    )
                ;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Delegate spring security exceptions to the Global exception handler.
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEntryPoint defaultAuthenticationEntryPoint(@SuppressWarnings(SPRING_APPLICATION_NAME)
                                                                        HandlerExceptionResolver handlerExceptionResolver) {

        return (request, response, authException) -> handlerExceptionResolver.resolveException(request, response, null, authException);
    }

    /**
     * Main object of the security part, responsible for managing all the authentication part.
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationManagerResolver<HttpServletRequest>
    defaultAuthenticationManagerResolver(@SuppressWarnings(SPRING_APPLICATION_NAME)
                                             IssuerHelper issuerHelper,
                                         Resolver<Issuer, JwtDecoder> issuerJwtDecoderResolver,
                                         Resolver<Issuer, Converter<Jwt, AbstractAuthenticationToken>> converterJwtAuthResolver,
                                         Resolver<Issuer, RequestMatcher> requestMatcherIntrospectResolver,
                                         Resolver<AuthenticationManagerDataCache, JwtAuthenticationProvider> jwtAuthenticationProviderResolver,
                                         Resolver<AuthenticationManagerDataCache, OpaqueTokenAuthenticationProvider> opaqueTokenAuthenticationProviderResolver
    ) {

        return new AuthenticationManagerResolverByIssuer(
            issuerHelper,
            issuerJwtDecoderResolver,
            converterJwtAuthResolver,
            requestMatcherIntrospectResolver,
            jwtAuthenticationProviderResolver,
            opaqueTokenAuthenticationProviderResolver
        );
    }

    /**
     * Makes room for customization of small parts.
     */
    @SuppressWarnings(SPRING_APPLICATION_NAME)
    @Bean
    @ConditionalOnMissingBean
    public ConverterJwtAuthResolverByIssuer defaultConverterJwtAuthResolverByIssuer(IssuerHelper issuerHelper) {
        return new ConverterJwtAuthResolverByIssuer(issuerHelper);
    }

    /**
     * Makes room for customization of small parts.
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtDecoderResolverByIssuer defaultJwtDecoderResolverByIssuer(Resolver<Issuer, Cache> issuerCacheResolver) {
        return new JwtDecoderResolverByIssuer(issuerCacheResolver);
    }

    /**
     * Makes room for customization of small parts.
     */
    @Bean
    @ConditionalOnMissingBean
    public RequestMatcherIntrospectResolverByIssuer defaultRequestMatcherIntrospectResolverByIssuer() {
        return new RequestMatcherIntrospectResolverByIssuer();
    }

    /**
     * Makes room for customization of small parts.
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationProviderResolverByIssuer defaultJwtAuthenticationProviderResolverByIssuer() {
        return new JwtAuthenticationProviderResolverByIssuer();
    }

    /**
     * Makes room for customization of small parts.
     */
    @Bean
    @ConditionalOnMissingBean
    public OpaqueTokenAuthenticationProviderResolverByIssuer defaultOpaqueTokenAuthenticationProviderResolverByIssuer() {
        return new OpaqueTokenAuthenticationProviderResolverByIssuer();
    }

    /**
     * Makes room for customization of small parts.
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheJwksResolverByIssuer defaultCacheJwksResolverByIssuer() {
        return new CacheJwksResolverByIssuer();
    }
}