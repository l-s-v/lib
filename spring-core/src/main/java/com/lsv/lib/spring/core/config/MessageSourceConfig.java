package com.lsv.lib.spring.core.config;

import com.lsv.lib.spring.core.message.MessageProperties;
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

    @Bean
    public MessageSource messageSource(MessageProperties messageProperties) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setBasenames(Arrays.stream(messageProperties.getFileNames())
                .map(s -> "classpath:" + s)
                .toArray(String[]::new));

        return messageSource;
    }
}