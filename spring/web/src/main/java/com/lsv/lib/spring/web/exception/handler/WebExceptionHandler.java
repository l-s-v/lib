package com.lsv.lib.spring.web.exception.handler;

import com.lsv.lib.core.exception.helper.HelperExceptionHandler;
import com.lsv.lib.core.exception.helper.ProblemDetail;
import jakarta.servlet.RequestDispatcher;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.Nullable;
import org.springframework.web.ErrorResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;

/**
 * Defines the default for web exception handlers.
 *
 * @author Leandro da Silva Vieira
 */
public interface WebExceptionHandler<IN, OUT> {

    OUT handleInternal(Exception exception, @Nullable Object body, HttpStatusCode httpStatusCode, IN in);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    default OUT createProblemDetail(Exception exception, MessageSource messageSource, IN in) {
        var problemDetail = resolveInstance(HelperExceptionHandler.handle(exception), in);

        var httpStatusCode = HttpStatusCode.valueOf(problemDetail.getStatus());
        var errorResponseBuilder = ErrorResponse.builder(exception, httpStatusCode, problemDetail.getDetail())
            .instance(ObjectUtils.isEmpty(problemDetail.getInstance()) ? null : URI.create(problemDetail.getInstance()))
            .detail(problemDetail.getDetail())
            .title(problemDetail.getTitle());

        if (problemDetail.getProperties() != null) {
            problemDetail.getProperties().forEach((key, value) -> errorResponseBuilder.property(key, value));
        }

        var body = errorResponseBuilder.build().updateAndGetBody(messageSource, LocaleContextHolder.getLocale());

        return handleInternal(exception, body, httpStatusCode, in);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private ProblemDetail resolveInstance(ProblemDetail problemDetail, IN in) {
        if (ObjectUtils.isEmpty(problemDetail.getInstance()) && in instanceof WebRequest webRequest) {
            var instance = webRequest.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI, RequestAttributes.SCOPE_REQUEST);
            if(ObjectUtils.isNotEmpty(instance)) {
                problemDetail.setInstance(String.valueOf(instance));
            }
        }
        return problemDetail;
    }
}