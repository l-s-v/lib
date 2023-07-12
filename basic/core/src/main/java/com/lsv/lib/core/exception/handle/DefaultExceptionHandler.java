package com.lsv.lib.core.exception.handle;

import com.google.auto.service.AutoService;
import com.lsv.lib.core.annotation.Priority;
import com.lsv.lib.core.exception.helper.HelperExceptionHandler;
import com.lsv.lib.core.exception.helper.ProblemDetail;
import com.lsv.lib.core.exception.message.MessageDisplayExceptionEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Leandro da Silva Vieira
 */
@Slf4j
@Priority
@AutoService(ExceptionHandleable.class)
public class DefaultExceptionHandler implements ExceptionHandleable<Throwable> {

    @Override
    public ProblemDetail handle(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);

        return HelperExceptionHandler.createProblemDetail(
                MessageDisplayExceptionEnum.SERVER_ERROR,
                null,
                ProblemDetail.INTERNAL_SERVER_ERROR);
    }
}