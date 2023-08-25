package com.lsv.lib.spring.web.resilient4j.exception.handle;

import com.google.auto.service.AutoService;
import com.lsv.lib.core.annotation.Priority;
import com.lsv.lib.core.exception.handle.ExceptionHandleable;
import com.lsv.lib.core.exception.helper.HelperExceptionHandler;
import com.lsv.lib.core.exception.helper.ProblemDetail;
import com.lsv.lib.core.exception.message.MessageDisplayExceptionEnum;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Leandro da Silva Vieira
 */
@Slf4j
@Priority
@AutoService(ExceptionHandleable.class)
public class CallNotPermittedExceptionHandler implements ExceptionHandleable<CallNotPermittedException> {

    private static final int SERVICE_UNAVAILABLE = 503;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public ProblemDetail handle(CallNotPermittedException callNotPermittedException) {
        log.error(callNotPermittedException.getMessage(), callNotPermittedException);

        return HelperExceptionHandler.createProblemDetail(
                MessageDisplayExceptionEnum.SERVER_ERROR,
                null,
            SERVICE_UNAVAILABLE);
    }
}