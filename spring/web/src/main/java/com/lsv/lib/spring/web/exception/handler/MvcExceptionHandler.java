package com.lsv.lib.spring.web.exception.handler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Leandro da Silva Vieira
 */
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MvcExceptionHandler
        extends ResponseEntityExceptionHandler
        implements WebExceptionHandler<WebRequest, ResponseEntity> {

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> handleGlobalException(Exception exception, WebRequest request) {
        return createProblemDetail(exception, getMessageSource(), request);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public ResponseEntity<Object> handleInternal(Exception exception, Object body, HttpStatusCode httpStatusCode, WebRequest request) {
        return handleExceptionInternal(exception, body, new HttpHeaders(), httpStatusCode, request);
    }
}