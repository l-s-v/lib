package com.lsv.lib.core.helper;

import com.lsv.lib.core.loader.Loader;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Provides utility methods for validating beans.
 *
 * @author Leandro da Silva Vieira
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HelperBeanValidation {

    private static final Validator validator = Loader.of(Validator.class).findUniqueImplementationByFirstLoader();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <T> Set<ConstraintViolation<T>> validateWhithoutThrow(T bean) {
        return getValidator().validate(bean);
    }

    public static <T> T validate(T bean) {
        throwsViolations(validateWhithoutThrow(bean));
        return bean;
    }
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static Validator getValidator() {
        return validator != null ? validator : Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static <T> void throwsViolations(Set<ConstraintViolation<T>> violations) {
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}