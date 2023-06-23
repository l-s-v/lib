package com.lsv.lib.spring.core.annotation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Leandro da Silva Vieira
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)

@Configuration
@ConfigurationProperties
public @interface LibConfigurationProperties {

    String BASE_NAME_PROPERTIES = "lsv.";
    String LIB_NAME_PROPERTIES = BASE_NAME_PROPERTIES + "lib.";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @AliasFor(
            annotation = ConfigurationProperties.class
    )
    String value() ;
}