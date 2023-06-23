package com.lsv.lib.core.exception;

import lombok.Getter;
import lombok.NonNull;

/**
 * Exception for business validations.
 *
 * @author Leandro da Silva Vieira
 */
@Getter
public class BusinessException extends ProblemDetailException {

    public BusinessException(ProblemDetail problemDetail) {
        super(problemDetail);
    }

    public BusinessException(@NonNull String message, Object ... detailMessageArguments) {
        this(new ProblemDetail()
                .detail(message)
                .detailMessageArguments(detailMessageArguments));
    }
}