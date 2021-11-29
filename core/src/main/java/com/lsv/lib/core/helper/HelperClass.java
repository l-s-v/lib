package com.lsv.lib.core.helper;

import lombok.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Fornece métodos utilitários para trabalhar com classes.
 *
 * @author Leandro da Silva Vieira
 */
public final class HelperClass {

    public static Class<?> identifyGenericsClass(@NonNull Object objSource) {
        return identifyGenericsClass(objSource, Object.class);
    }

    public static <R> Class<R> identifyGenericsClass(@NonNull Object objSource, @NonNull Class<R> wantedClass) {
        return identifyGenericsClassByType(objSource, wantedClass);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    private static <R> Class<R> identifyGenericsClassByType(@NonNull Object objSource, Class<R> wantedClass) {
        List<Type> types = new LinkedList<>();
        types.add(objSource.getClass().getGenericSuperclass());
        types.addAll(Arrays.asList(objSource.getClass().getGenericInterfaces()));

        for (Type type : types) {
            if(type instanceof ParameterizedType) {
                for (Type arguments : ((ParameterizedType) type).getActualTypeArguments()) {
                    if (arguments instanceof Class<?> && wantedClass.isAssignableFrom((Class<?>) arguments)) {
                        return (Class<R>) arguments;
                    }
                }
            }
        }

        throw new NoSuchElementException("Type not found among generics.");
    }
}