package com.lsv.lib.core.exception;

import com.lsv.lib.core.exception.helper.ProblemDetail;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * Lets you inform properties that comply with RFC7807.
 *
 * @author Leandro da Silva Vieira
 */
@Getter
@Accessors(fluent = true)
public class ProblemDetailException extends RuntimeException {

    private ProblemDetail problemDetail;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public ProblemDetailException(@NonNull ProblemDetail problemDetail) {
        this(problemDetail, null);
    }

    public ProblemDetailException(@NonNull ProblemDetail problemDetail, Throwable cause) {
        super(problemDetail.getDetail(), cause);
        this.problemDetail = problemDetail;
    }
}