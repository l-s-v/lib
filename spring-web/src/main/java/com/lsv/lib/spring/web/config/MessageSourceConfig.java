package com.lsv.lib.spring.web.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author Leandro da Silva Vieira
 */
@Configuration
public class MessageSourceConfig {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Bean
    public MessageSource messageSource(WebProperties webProperties) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setBasenames(Arrays.stream(webProperties.getMessageSourceBaseNames())
                .map(s -> "classpath:" + s)
                .toArray(String[]::new));

        return messageSource;
    }
}