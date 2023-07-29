package com.lsv.lib.spring.security.web.resourceserver.exception.handler;

import com.google.auto.service.AutoService;
import com.lsv.lib.core.annotation.Priority;
import com.lsv.lib.core.exception.handle.ExceptionHandleable;
import com.lsv.lib.core.exception.handle.UnauthorizedExceptionHandler;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;

/**
 * @author Leandro da Silva Vieira
 */
@Priority
@AutoService(ExceptionHandleable.class)
public class InvalidBearerTokenExceptionHandler extends UnauthorizedExceptionHandler<InvalidBearerTokenException> {
}