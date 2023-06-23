package com.lsv.lib.core.exception;

import lombok.Getter;
import lombok.NonNull;

/**
 * Lets you inform properties that comply with RFC7807.
 *
 * @author Leandro da Silva Vieira
 */
@Getter
public class ProblemDetailException extends RuntimeException {

    private ProblemDetail problemDetail;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public ProblemDetailException(@NonNull ProblemDetail problemDetail) {
        super(problemDetail.detail());
        this.problemDetail = problemDetail;
    }
}