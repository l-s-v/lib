package com.lsv.lib.core.exception.handle;

import com.lsv.lib.core.annotation.Priority;
import com.lsv.lib.core.exception.helper.HelperExceptionHandler;
import com.lsv.lib.core.exception.helper.ProblemDetail;
import com.lsv.lib.core.exception.message.MessageDisplayExceptionEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the pattern for classes that handle access unauthorized exceptions.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
@Priority
public abstract class UnauthorizedExceptionHandler<T extends Throwable> implements ExceptionHandleable<T> {

    @Override
    public ProblemDetail handle(T unauthorizedException) {
        log.debug(unauthorizedException.getMessage(), unauthorizedException);

        return HelperExceptionHandler.createProblemDetail(
            MessageDisplayExceptionEnum.UNAUTHORIZED,
            null,
            ProblemDetail.UNAUTHORIZED);
    }
}