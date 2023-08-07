package com.lsv.lib.spring.security.web.config;

import com.lsv.lib.spring.security.web.annotation.ConditionalWebSecurityEnable;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

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
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/**");
    }
}