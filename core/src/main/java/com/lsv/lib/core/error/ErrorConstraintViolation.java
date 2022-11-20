package com.lsv.lib.core.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Setter(AccessLevel.PRIVATE)
public class ErrorConstraintViolation implements Error<ConstraintViolationException> {

    private List<Violation> violations;

    @Override
    public Error<ConstraintViolationException> create(ConstraintViolationException throwable) {
        log.debug(throwable.getMessage(), throwable);

        return setViolations(throwable.getConstraintViolations().stream()
                .map(Violation::of)
                .toList());
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