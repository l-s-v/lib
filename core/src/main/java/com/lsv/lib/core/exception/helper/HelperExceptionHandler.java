package com.lsv.lib.core.exception.helper;

import com.lsv.lib.core.exception.handle.DefaultExceptionHandler;
import com.lsv.lib.core.exception.handle.ExceptionHandleable;
import com.lsv.lib.core.exception.message.MessageDisplayException;
import com.lsv.lib.core.exception.message.MessageDisplayExceptionEnum;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.loader.Loader;
import com.lsv.lib.core.message.MessageSource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Leandro da Silva Vieira
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HelperExceptionHandler {

    private static final String TITLE = "%s_TITLE";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static Map<Class<?>, ExceptionHandleable<?>> implementationsCache = new HashMap<>();
    private static ExceptionHandleable<?> defaultExceptionHandler = new DefaultExceptionHandler();
    private static MessageSource messageSource;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <T extends Throwable> ProblemDetail handle(@NonNull T throwable) {
        if (implementationsCache == null) {
            implementationsCache = new HashMap<>();
        }

        ExceptionHandleable<T> exceptionHandleable = (ExceptionHandleable<T>) implementationsCache.get(throwable.getClass());

        if (exceptionHandleable == null) {
            exceptionHandleable = Loader.of(ExceptionHandleable.class)
                    .findImplementationsByAllLoaders()
                    .stream()
                    .filter(errorResponse -> validType(throwable, errorResponse))
                    .findFirst()
                    .orElse(defaultExceptionHandler);

            implementationsCache.put(throwable.getClass(), exceptionHandleable);
        }

        return exceptionHandleable.handle(throwable);
    }

    public static ProblemDetail createProblemDetail(@NonNull MessageDisplayException messageDisplayException,
                                                    String detail, Integer status,
                                                    Object... detailMessageArguments) {


        try {
            String title = messageSource().message(TITLE.formatted(messageDisplayException.name()));
            if (ObjectUtils.isEmpty(title)) {
                title = messageDisplayException.name();
            }

            if (ObjectUtils.isEmpty(detail)) {
                detail = messageSource().message(messageDisplayException.name(), detailMessageArguments);

                if (ObjectUtils.isEmpty(detail)) {
                    detail = messageSource().message(MessageDisplayExceptionEnum.MESSAGE_NOT_FOUND.name(), messageDisplayException.name());
                }
            }

            return ProblemDetail.of()
                    .title(title)
                    .detail(detail)
                    .status(status)
                    .get();
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return ProblemDetail.of()
                    .title(messageDisplayException.name())
                    .detail(ObjectUtils.isNotEmpty(detail) ? detail : messageDisplayException.name())
                    .status(status)
                    .get();
        }
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static <T extends Throwable> boolean validType(T throwable, ExceptionHandleable<?> errorResponse) {
        return throwable.getClass().equals(HelperClass.identifyGenericsClass(errorResponse));
    }

    private static MessageSource messageSource() {
        if (messageSource == null) {
            messageSource = Loader.of(MessageSource.class).findUniqueImplementationByFirstLoader();
        }
        return messageSource;
    }
}