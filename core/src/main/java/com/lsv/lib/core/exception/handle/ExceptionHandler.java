package com.lsv.lib.core.exception.handle;

import com.google.auto.service.AutoService;
import com.lsv.lib.core.annotation.Priority;
import com.lsv.lib.core.exception.ProblemDetail;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Leandro da Silva Vieira
 */
@Slf4j
@Priority
@AutoService(ExceptionHandleable.class)
public class ExceptionHandler implements ExceptionHandleable<Throwable> {

    @Override
    public ProblemDetail create(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);

        return new ProblemDetail()
                .detail(throwable.getMessage())
                .status(ProblemDetail.INTERNAL_SERVER_ERROR);
    }
}