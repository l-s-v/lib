package com.lsv.lib.spring.core.config;

import com.lsv.lib.core.i18n.I18nSource;
import com.lsv.lib.spring.core.properties.SpringI18nProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author Leandro da Silva Vieira
 */
@EnableConfigurationProperties(SpringI18nProperties.class)
@AutoConfiguration
public class SpringI18nAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public MessageSource messageSource(SpringI18nProperties i18nProperties) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setBasenames(Arrays.stream(i18nProperties.getFileNames())
                .map(s -> "classpath:" + s)
                .toArray(String[]::new));

        return messageSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public I18nSource defaultI18nSource(MessageSource messageSource) {
        return (code, defaultMessage, locale, args) -> {
            if (locale == null) {
                locale = LocaleContextHolder.getLocale();
            }

            return messageSource.getMessage(code, args, defaultMessage, locale);
        };
    }
}