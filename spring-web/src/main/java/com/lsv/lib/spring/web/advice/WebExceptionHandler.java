package com.lsv.lib.spring.web.advice;

import com.lsv.lib.core.exception.handle.HelperExceptionHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.Nullable;
import org.springframework.web.ErrorResponse;

/**
 * @author Leandro da Silva Vieira
 */
public interface WebExceptionHandler<IN, OUT> {

    OUT handleInternal(Exception exception, @Nullable Object body, HttpStatusCode httpStatusCode, IN in);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    default OUT createProblemDetail(Exception exception, MessageSource messageSource, IN in) {
        var problemDetail = HelperExceptionHandler.of(exception);
        var httpStatusCode = HttpStatusCode.valueOf(problemDetail.status());
        var errorResponseBuilder = ErrorResponse.builder(exception, httpStatusCode, problemDetail.detail())
                .detailMessageCode(problemDetail.detail())
                .detailMessageArguments(problemDetail.detailMessageArguments())
                .title(problemDetail.title());

        if (problemDetail.properties() != null) {
            problemDetail.properties().forEach((key, value) -> errorResponseBuilder.property(key, value));
        }

        var body = errorResponseBuilder.build().updateAndGetBody(messageSource, LocaleContextHolder.getLocale());

        return handleInternal(exception, body, httpStatusCode, in);
    }
}