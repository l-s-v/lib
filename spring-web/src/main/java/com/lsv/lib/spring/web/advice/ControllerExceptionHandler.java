package com.lsv.lib.spring.web.advice;

import com.lsv.lib.core.error.Error;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseBody
    Error<?> handleMethodArgumentNotValidException(NoSuchElementException e) {
        return Error.of(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    Error<?> handleMethodArgumentNotValidException(ConstraintViolationException e) {
        return Error.of(e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    Error<?> handleMethodArgumentNotValidException(Exception e) {
        return Error.of(e);
    }
}