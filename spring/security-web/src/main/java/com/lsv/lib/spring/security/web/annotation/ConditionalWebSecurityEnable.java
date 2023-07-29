package com.lsv.lib.spring.security.web.annotation;

import com.lsv.lib.spring.security.web.properties.SpringSecurityWebProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Leandro da Silva Vieira
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnProperty(
    prefix = SpringSecurityWebProperties.PATH,
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true
)
public @interface ConditionalWebSecurityEnable {

    @AliasFor(annotation = ConditionalOnProperty.class, attribute = "havingValue")
    String havingValue() default "true";

    @AliasFor(annotation = ConditionalOnProperty.class, attribute = "matchIfMissing")
    boolean matchIfMissing() default true;
}