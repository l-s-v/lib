package com.lsv.lib.spring.security.web.config;

import com.lsv.lib.core.function.ExtraProcess;
import com.lsv.lib.security.web.AllowHttpAccess;
import com.lsv.lib.security.web.properties.HttpMatcher;
import com.lsv.lib.spring.core.config.SpringCoreAutoConfig;
import com.lsv.lib.spring.security.web.annotation.ConditionalWebSecurityEnable;
import com.lsv.lib.security.web.helper.oidc.IssuerHelper;
import com.lsv.lib.spring.security.web.helper.SpringSecurityWebHelperConfig;
import com.lsv.lib.spring.security.web.properties.SpringSecurityWebOauth2Properties;
import com.lsv.lib.spring.security.web.properties.SpringSecurityWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.lsv.lib.spring.core.helper.ConstantsSpring.SPRING_APPLICATION_NAME;
import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Autoconfiguration for the spring-security-web module.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j

@ConditionalWebSecurityEnable
@AutoConfiguration(after = {SpringCoreAutoConfig.class})
@EnableConfigurationProperties({SpringSecurityWebProperties.class, SpringSecurityWebOauth2Properties.class})
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
@EnableWebSecurity
public class SpringSecurityWebAutoConfig {

    /**
     * Default security settings.
     */
    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain defaultEnabledSecurityFilterChain(HttpSecurity httpSecurity,
                                                                 List<AllowHttpAccess> allowHttpsAccess,
                                                                 List<ExtraProcess<HttpSecurity>> httpSecurityExtraProcesses,
                                                                 @SuppressWarnings(SPRING_APPLICATION_NAME)
                                                                     SpringSecurityWebHelperConfig springSecurityWebHelperConfig
    ) throws Exception {

        log.trace("Iniciando configuração de segurança");
        springSecurityWebHelperConfig.validateConfig();

        log.trace("Processando configurações extras para HttpSecurity");
        httpSecurityExtraProcesses.forEach(extraProcess -> extraProcess.process(httpSecurity));

        log.trace("Adicionando patterns permiteAll");
        configureAuthorizeHttpRequests(httpSecurity, allowHttpsAccess.stream()
            .flatMap(allowHttpAccess -> allowHttpAccess.get().stream())
            .toList());

        defaultSecurity(httpSecurity);

        return httpSecurity.build();
    }

    /**
     * Releases the accesses defined in the PermitAll property.
     */
    @Bean
    public AllowHttpAccess permiteAllAllowHttpAccess(SpringSecurityWebProperties springSecurityWebProperties) {
        return () -> springSecurityWebProperties.getHttpRequestsPermitAll();
    }

    /**
     * Integrating CorsFilter with Spring Security.
     * <p>
     * https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html
     */
    @Bean
    @ConditionalOnMissingBean
    public CorsConfigurationSource defaultCorsConfigurationSource(SpringSecurityWebProperties springSecurityWebProperties) {
        if (ObjectUtils.isEmpty(springSecurityWebProperties.getCors())) {
            return null;
        }

        var corsConfigurationSource = new UrlBasedCorsConfigurationSource();

        for (var cors : springSecurityWebProperties.getCors()) {
            var corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(cors.getAllowedOrigins());
            corsConfiguration.setAllowedMethods(cors.getAllowedMethods());
            corsConfiguration.setAllowedHeaders(cors.getAllowedHeaders());
            corsConfiguration.setExposedHeaders(cors.getExposedHeaders());

            corsConfigurationSource.registerCorsConfiguration(cors.getPath(), corsConfiguration);
        }

        return corsConfigurationSource;
    }

    /**
     * Default issuerHelper.
     */
    @Bean
    @ConditionalOnMissingBean
    public IssuerHelper defaultIssuerHelper(SpringSecurityWebOauth2Properties springSecurityWebOauth2Properties) {
        return new IssuerHelper(springSecurityWebOauth2Properties);
    }

    /**
     * Makes room for customization of small parts.
     */
    @Bean
    @ConditionalOnMissingBean
    public SpringSecurityWebHelperConfig defaultSpringSecurityWebHelperConfig(IssuerHelper issuerHelper) {
        return new SpringSecurityWebHelperConfig(issuerHelper);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void configureAuthorizeHttpRequests(HttpSecurity httpSecurity, List<HttpMatcher> httpRequestsPermitAll) throws Exception {
        if (ObjectUtils.isNotEmpty(httpRequestsPermitAll)) {
            httpSecurity
                .authorizeHttpRequests(authorization ->
                    httpRequestsPermitAll
                        .forEach(matcher -> authorization
                            .requestMatchers(matcher.getMethod() != null ? HttpMethod.valueOf(matcher.getMethod()) : null, matcher.getPattern())
                            .permitAll()
                        ));
        }
    }

    private void defaultSecurity(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .cors(withDefaults())
            // For everything else it will require the user to be authenticated
            .authorizeHttpRequests(authorization -> authorization
                .requestMatchers("/**").authenticated()
                .anyRequest().authenticated())
            // Removes the "continue" parameter that Spring Security 6 adds to the url to
            // tell it to search the cache for SavedRequest. But this will disable this feature.
            // https://docs.spring.io/spring-security/reference/migration/servlet/session-management.html#requestcache-query-optimization
            .requestCache(cache -> {
                HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
                requestCache.setMatchingRequestParameterName(null);
                cache.requestCache(requestCache);
            })
        ;
    }
}