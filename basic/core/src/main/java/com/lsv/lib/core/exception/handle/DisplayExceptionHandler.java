package com.lsv.lib.core.exception.handle;

import com.google.auto.service.AutoService;
import com.lsv.lib.core.annotation.Priority;
import com.lsv.lib.core.exception.DisplayException;
import com.lsv.lib.core.exception.helper.ProblemDetail;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Leandro da Silva Vieira
 */
@Slf4j
@Priority
@AutoService(ExceptionHandleable.class)
public class DisplayExceptionHandler implements ExceptionHandleable<DisplayException> {

    @Override
    public ProblemDetail handle(DisplayException displayException) {
        log.debug("{}", displayException.problemDetail());

        return displayException.problemDetail();
    }
}