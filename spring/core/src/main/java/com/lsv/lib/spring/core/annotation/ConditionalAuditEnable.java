package com.lsv.lib.spring.core.annotation;

import com.lsv.lib.core.audit.AuditProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Annotation to make it easy to check if auditing is enabled for the specific module.
 * Just change the prefix.
 *
 * @author Leandro da Silva Vieira
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnProperty(
    havingValue = "true",
    matchIfMissing = true
)
public @interface ConditionalAuditEnable {

    @AliasFor(annotation = ConditionalOnProperty.class, attribute = "prefix")
    String value();

    @AliasFor(annotation = ConditionalOnProperty.class, attribute = "name")
    String name() default AuditProperties.PATH + "." + AuditProperties.PROP_ENABLED;
}