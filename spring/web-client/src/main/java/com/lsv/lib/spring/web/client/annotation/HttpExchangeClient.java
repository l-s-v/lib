package com.lsv.lib.spring.web.client.annotation;

import com.lsv.lib.spring.web.client.properties.WebClientModuleProperties;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.service.annotation.HttpExchange;

import java.lang.annotation.*;

/**
 * Annotation used to identify the interfaces and automate the process of generating the required beans.
 *
 * @author Leandro da Silva Vieira
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@HttpExchange
public @interface HttpExchangeClient {

    String ATTR_CONFIGURATION_ID = "configurationId";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Alias for {@link HttpExchange#value}.
     */
    @AliasFor(annotation = HttpExchange.class)
    String value() default "";

    /**
     * Alias for {@link HttpExchange#url()}.
     */
    @AliasFor(annotation = HttpExchange.class)
    String url() default "";

    /**
     * Alias for {@link HttpExchange#contentType()}.
     */
    @AliasFor(annotation = HttpExchange.class)
    String contentType() default "";

    /**
     * Alias for {@link HttpExchange#accept()}.
     */
    @AliasFor(annotation = HttpExchange.class)
    String[] accept() default {};

    /**
     * Identifier of the configurations made in the properties file.
     *
     * @see WebClientModuleProperties#getConfigurations()
     */
    String configurationId() default "";
}