package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.exception.ProblemDetailException;
import com.lsv.lib.core.exception.helper.HelperExceptionHandler;
import com.lsv.lib.core.exception.message.MessageDisplayExceptionEnum;
import com.lsv.lib.spring.web.exception.handler.MvcExceptionHandler;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import static com.lsv.lib.spring.web.controller.MvcErrorController.REQUEST_MAPPING;

/**
 * Replaces the original spring controller for error handling.
 * Recovers the error and directs to the already defined handler.
 *
 * @author Leandro da Silva Vieira
 */
@Hidden
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.ANY)
@RestController
@RequestMapping(REQUEST_MAPPING)
public class MvcErrorController extends AbstractErrorController {

    private MvcExceptionHandler mvcExceptionHandler;
    private ErrorAttributes errorAttributes;

    public static final String REQUEST_MAPPING = "${server.error.path:${error.path:/error}}";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public MvcErrorController(ErrorAttributes errorAttributes,
                              MvcExceptionHandler mvcExceptionHandler) {
        super(errorAttributes);

        this.errorAttributes = errorAttributes;
        this.mvcExceptionHandler = mvcExceptionHandler;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @GetMapping
    public Object error(HttpServletRequest request) {
        return mvcExceptionHandler.handleGlobalException(resolveException(request), new ServletWebRequest(request));
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private Exception resolveException(HttpServletRequest request) {
        var exception = (Exception) errorAttributes.getError(new ServletWebRequest(request));
        if (exception == null) {
            var error = getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.values()));
            var status = (Integer) error.get("status");

            exception = new ProblemDetailException(
                HelperExceptionHandler.createProblemDetail(
                        MessageDisplayExceptionEnum.valueOf(status),
                        null,
                        status)
                    .setInstance((String) error.get("path")));
        }

        return exception;
    }
}