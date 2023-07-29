package com.lsv.lib.spring.security.web.config;

import com.lsv.lib.spring.security.web.annotation.ConditionalWebSecurityEnable;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static com.lsv.lib.spring.core.helper.ConstantsSpring.SPRING_APPLICATION_NAME;

/**
 * Autoconfiguration for when security is not enabled.
 *
 * @author Leandro da Silva Vieira
 */
@ConditionalWebSecurityEnable(
    havingValue = "false",
    matchIfMissing = false
)
@AutoConfiguration
class SpringSecurityWebDisabledAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain disabledSecurityFilterChain(@SuppressWarnings(SPRING_APPLICATION_NAME)
                                                               HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            // Following the pattern of the previous library
            .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
            .headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
            .authorizeHttpRequests(authorization -> authorization
                .requestMatchers("/**").permitAll()
            )
        ;
        return httpSecurity.build();
    }
}