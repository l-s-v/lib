package com.lsv.lib.core.error;

import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.loader.Loader;
import lombok.NonNull;

import java.io.Serializable;

public interface Error<T extends Throwable> extends Serializable {

    Error<T> create(T throwable);

    @SuppressWarnings("unchecked")
    static <T extends Throwable> Error<T> of(@NonNull T throwable) {
        return Loader.of(Error.class)
                .findImplementationsByReflection(Error.class.getPackageName())
                .implementations().stream()
                .filter(errorResponse -> validType(throwable, errorResponse))
                .findFirst()
                .orElse(new ErrorDefault())
                .create(throwable);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static <T extends Throwable> boolean validType(T throwable, Error<?> errorResponse) {
        return throwable.getClass().equals(HelperClass.identifyGenericsClass(errorResponse));
    }
}