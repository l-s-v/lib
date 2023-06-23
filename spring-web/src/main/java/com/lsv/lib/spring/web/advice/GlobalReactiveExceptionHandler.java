package com.lsv.lib.spring.web.advice;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Leandro da Silva Vieira
 */
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class GlobalReactiveExceptionHandler
        extends ResponseEntityExceptionHandler
        implements WebExceptionHandler<ServerWebExchange, Mono> {

    @ExceptionHandler(Exception.class)
    Mono<ResponseEntity<Object>> handleMethodArgumentNotValidException(Exception exception, ServerWebExchange exchange) {
        return createProblemDetail(exception, getMessageSource(), exchange);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public Mono<ResponseEntity<Object>> handleInternal(Exception exception, Object body, HttpStatusCode httpStatusCode, ServerWebExchange exchange) {
        return handleExceptionInternal(exception, body, new HttpHeaders(), httpStatusCode, exchange);
    }
}