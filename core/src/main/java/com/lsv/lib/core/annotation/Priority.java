package com.lsv.lib.core.annotation;

import java.lang.annotation.*;

/**
 * @author Leandro da Silva Vieira
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Priority {

    int value() default 0;
}