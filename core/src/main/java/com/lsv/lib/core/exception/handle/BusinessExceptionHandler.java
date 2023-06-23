package com.lsv.lib.core.exception.handle;

import com.google.auto.service.AutoService;
import com.lsv.lib.core.annotation.Priority;
import com.lsv.lib.core.exception.BusinessException;
import com.lsv.lib.core.exception.ProblemDetail;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Leandro da Silva Vieira
 */
@Slf4j
@Priority
@AutoService(ExceptionHandleable.class)
public class BusinessExceptionHandler implements ExceptionHandleable<BusinessException> {

    @Override
    public ProblemDetail create(BusinessException businessException) {
        log.debug("{}", businessException.problemDetail());

        return businessException.problemDetail();
    }
}