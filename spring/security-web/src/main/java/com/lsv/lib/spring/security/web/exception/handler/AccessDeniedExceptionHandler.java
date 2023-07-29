package com.lsv.lib.spring.security.web.exception.handler;

import com.google.auto.service.AutoService;
import com.lsv.lib.core.annotation.Priority;
import com.lsv.lib.core.exception.handle.ForbiddenExceptionHandler;
import com.lsv.lib.core.exception.handle.ExceptionHandleable;
import org.springframework.security.access.AccessDeniedException;

/**
 * @author Leandro da Silva Vieira
 */
@Priority
@AutoService(ExceptionHandleable.class)
public class AccessDeniedExceptionHandler extends ForbiddenExceptionHandler<AccessDeniedException> {
}