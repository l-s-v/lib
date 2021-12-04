package com.lsv.lib.core.helper;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.service.validations.TypeOperation;
import com.lsv.lib.core.concept.service.validations.Validable;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Fornece métodos utilitários para validação de beans.
 *
 * @author Leandro da Silva Vieira
 */
public final class HelperBeanValidation {

    public static Validator getValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static <T> Set<ConstraintViolation<T>> validateWithViolations(T bean) {
        return getValidator().validate(bean);
    }

    public static <T> void validate(T bean) {
        throwsViolations(validateWithViolations(bean));
    }

    public static <T> void validate(List<Validable<T>> validables) {
        Optional.ofNullable(validables)
            .orElse(Collections.emptyList())
            .forEach(HelperBeanValidation::validate);
    }

    public static <T extends Identifiable<?>> void validate(List<Validable<T>> validables, T identifiable, TypeOperation typeOperation) {
        validate(
            Optional.ofNullable(validables)
                .orElse(Collections.emptyList())
                .stream().map(validable -> validable
                    .objValidable(identifiable)
                    .typeOperation(typeOperation))
                .collect(Collectors.toList()
                )
        );
    }
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static <T> void throwsViolations(Set<ConstraintViolation<T>> violations) {
        if(! violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}