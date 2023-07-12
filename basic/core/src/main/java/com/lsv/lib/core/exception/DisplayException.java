package com.lsv.lib.core.exception;

import com.lsv.lib.core.exception.helper.ProblemDetail;
import lombok.Getter;
import lombok.NonNull;

/**
 * Exception for business validations.
 *
 * @author Leandro da Silva Vieira
 */
@Getter
public class DisplayException extends ProblemDetailException {

    public DisplayException(@NonNull ProblemDetail problemDetail) {
        this(problemDetail, null);
    }

    public DisplayException(@NonNull ProblemDetail problemDetail, Throwable cause) {
        super(problemDetail, cause);
    }
}