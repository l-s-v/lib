package com.lsv.lib.core.exception.handle;

import com.google.auto.service.AutoService;
import com.lsv.lib.core.annotation.Priority;
import com.lsv.lib.core.exception.ProblemDetailException;
import com.lsv.lib.core.exception.helper.ProblemDetail;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Leandro da Silva Vieira
 */
@Slf4j
@Priority
@AutoService(ExceptionHandleable.class)
public class ProblemDetailExceptionHandler implements ExceptionHandleable<ProblemDetailException> {

    @Override
    public ProblemDetail handle(ProblemDetailException problemDetailException) {
        log.debug("{}", problemDetailException.problemDetail());

        return problemDetailException.problemDetail();
    }
}