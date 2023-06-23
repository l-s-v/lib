package com.lsv.lib.core.exception.handle;

import com.lsv.lib.core.exception.ProblemDetail;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.loader.Loader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Leandro da Silva Vieira
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HelperExceptionHandler {

    private static Map<Class<?>, ExceptionHandleable<?>> implementationsCache = new HashMap<>();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <T extends Throwable> ProblemDetail of(@NonNull T throwable) {
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
                    .orElse(new ExceptionHandler());

            implementationsCache.put(throwable.getClass(), exceptionHandleable);
        }

        return exceptionHandleable.create(throwable);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static <T extends Throwable> boolean validType(T throwable, ExceptionHandleable<?> errorResponse) {
        return throwable.getClass().equals(HelperClass.identifyGenericsClass(errorResponse));
    }
}