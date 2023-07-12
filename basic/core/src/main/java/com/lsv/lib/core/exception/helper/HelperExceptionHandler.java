package com.lsv.lib.core.exception.helper;

import com.lsv.lib.core.exception.handle.DefaultExceptionHandler;
import com.lsv.lib.core.exception.handle.ExceptionHandleable;
import com.lsv.lib.core.exception.message.MessageDisplayException;
import com.lsv.lib.core.exception.message.MessageDisplayExceptionEnum;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.i18n.I18nSource;
import com.lsv.lib.core.loader.Loader;
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

    private static final Map<Class<?>, ExceptionHandleable<?>> implementationsCache = new HashMap<>();
    private static final DefaultExceptionHandler defaultExceptionHandler = new DefaultExceptionHandler();
    private static I18nSource i18nSource;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> ProblemDetail handle(@NonNull T throwable) {
        log.trace("Procurando responsável por tratar a exceção {}", throwable.getClass().getName());

        var exceptionHandleable = (ExceptionHandleable<T>) implementationsCache.get(throwable.getClass());

        if (exceptionHandleable == null) {
            exceptionHandleable = Loader.of(ExceptionHandleable.class)
                    .findImplementationsByAllLoaders()
                    .stream()
                    .filter(errorResponse -> validType(throwable, errorResponse))
                    .findFirst()
                    .orElse(defaultExceptionHandler);

            log.trace("Registrando responsável por tratar a exceção {}", throwable.getClass().getName());

            implementationsCache.put(throwable.getClass(), exceptionHandleable);
        }

        return exceptionHandleable.handle(throwable);
    }

    public static ProblemDetail createProblemDetail(@NonNull MessageDisplayException messageDisplayException,
                                                    String detail, Integer status,
                                                    Object... detailMessageArguments) {


        try {
            String title = i18nSource().message(TITLE.formatted(messageDisplayException.name()));
            if (ObjectUtils.isEmpty(title)) {
                title = messageDisplayException.name();
            }

            if (ObjectUtils.isEmpty(detail)) {
                detail = i18nSource().message(messageDisplayException.name(), detailMessageArguments);

                if (ObjectUtils.isEmpty(detail)) {
                    detail = i18nSource().message(MessageDisplayExceptionEnum.MESSAGE_NOT_FOUND.name(), messageDisplayException.name());
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

    private static I18nSource i18nSource() {
        if (i18nSource == null) {
            i18nSource = Loader.of(I18nSource.class).findUniqueImplementationByFirstLoader();
        }
        return i18nSource;
    }
}