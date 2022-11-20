package com.lsv.lib.core.error;

import lombok.Getter;

/**
 * @author Leandro da Silva Vieira
 */
@Getter
public class ValidationException extends RuntimeException {

    private transient final Object objectValidation;

    public ValidationException(String message, Object objectValidation) {
        super(message);
        this.objectValidation = objectValidation;
    }

    public ValidationException(String message) {
        this(message, null);
    }
}