package com.lsv.lib.spring.web.exception.handler;

import com.lsv.lib.core.exception.ProblemDetailException;
import com.lsv.lib.core.helper.HelperBeanValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

import static com.lsv.lib.core.exception.message.MessageDisplayExceptionEnum.REQUEST_NOT_ACCEPTABLE;

/**
 * @author Leandro da Silva Vieira
 */
@Slf4j

@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MvcExceptionHandler
        extends ResponseEntityExceptionHandler
        implements WebExceptionHandler<WebRequest, ResponseEntity<Object>> {

    private static final List<Class<?>> EXCEPTIONS_HANDLED_SUPERCLASS = List.of(
        HttpRequestMethodNotSupportedException.class,
        HttpMediaTypeNotSupportedException.class,
        HttpMediaTypeNotAcceptableException.class,
        MissingPathVariableException.class,
        MissingServletRequestParameterException.class,
        MissingServletRequestPartException.class,
        ServletRequestBindingException.class,
        HandlerMethodValidationException.class,
        NoHandlerFoundException.class,
        NoResourceFoundException.class,
        AsyncRequestTimeoutException.class,
        ErrorResponseException.class,
        MaxUploadSizeExceededException.class,
        ConversionNotSupportedException.class,
        TypeMismatchException.class,
        HttpMessageNotWritableException.class,
        MethodValidationException.class,
        BindException.class
    );

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception exception, WebRequest request) {
        return createProblemDetail(exception, getMessageSource(), request);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        try {
            // Força que uma exceção de validação com as informações completas
            HelperBeanValidation.validate(e.getBindingResult().getTarget());
            return null;
        } catch (Exception ex) {
            return handleGlobalException(ex, request);
        }
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (e.getRootCause() != null && e.getRootCause() instanceof ProblemDetailException problemDetailException) {
            return handleGlobalException(problemDetailException, request );
        }
        return handleGlobalException(
            REQUEST_NOT_ACCEPTABLE.displayException(e.getMostSpecificCause().getMessage().split("\\n")[0]),
            request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        // Só escreve no log se for uma das exceções tratada apenas na classe original
        if(EXCEPTIONS_HANDLED_SUPERCLASS.contains(e.getClass())) {
            log.error(e.getMessage(), e);
        }
        return super.handleExceptionInternal(e, body, headers, statusCode, request);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public ResponseEntity<Object> handleInternal(Exception exception, Object body, HttpStatusCode httpStatusCode, WebRequest request) {
        return handleExceptionInternal(exception, body, new HttpHeaders(), httpStatusCode, request);
    }
}