package com.lsv.lib.core.exception.handle;

import com.google.auto.service.AutoService;
import com.lsv.lib.core.annotation.Priority;
import com.lsv.lib.core.exception.helper.HelperExceptionHandler;
import com.lsv.lib.core.exception.helper.ProblemDetail;
import com.lsv.lib.core.exception.message.MessageDisplayExceptionEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author Leandro da Silva Vieira
 */
@Slf4j
@Priority
@AutoService(ExceptionHandleable.class)
public class ConstraintViolationExceptionHandler implements ExceptionHandleable<ConstraintViolationException> {

    private static final String PROPERTY_VIOLATIONS = "violations";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public ProblemDetail handle(ConstraintViolationException constraintViolationException) {
        log.debug(constraintViolationException.getMessage(), constraintViolationException);

        return HelperExceptionHandler.createProblemDetail(
                MessageDisplayExceptionEnum.BEAN_VALIDATION,
                constraintViolationException.getMessage(),
                null
        ).property(PROPERTY_VIOLATIONS, constraintViolationException.getConstraintViolations().stream()
                .map(Violation::of)
                .toList())
                ;
    }

    @Getter
    @Setter
    @Builder
    private static class Violation {
        private String message;
        private String property;

        private static Violation of(ConstraintViolation<?> constraintViolation) {
            return Violation.builder()
                    .message(constraintViolation.getMessage())
                    .property(Optional.ofNullable((Object) constraintViolation.getPropertyPath()).orElse(Optional.empty()).toString())
                    .build();
        }
    }
}