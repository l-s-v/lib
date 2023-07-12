package com.lsv.lib.core.helper;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ClassUtils;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Fornece métodos utilitários para trabalhar com classes.
 *
 * @author Leandro da Silva Vieira
 */
public final class HelperClass {

    private static final NoSuchElementException NO_SUCH_ELEMENT_EXCEPTION =
            new NoSuchElementException("Type not found among generics.");

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static Class<?> identifyGenericsClass(@NonNull Object objSource) {
        return identifyGenericsClass(objSource, Object.class);
    }

    public static <R> Class<R> identifyGenericsClass(@NonNull Object objSource, @NonNull Class<R> wantedClass) {
        return identifyGenericsClassByType(objSource, t -> t instanceof Class<?> && wantedClass.isAssignableFrom((Class<?>) t));
    }

    public static <R> Class<R> identifyGenericsClass(@NonNull Object objSource, int position) {
        AtomicInteger count = new AtomicInteger();
        return identifyGenericsClassByType(objSource, t -> t instanceof Class<?> && count.incrementAndGet() > position);
    }

    public static Class<?> findDirectSuperclassOrInterface(@NonNull Object objSource, @NonNull Class<?> superclassOrInterface) {
        List<Class<?>> classes = new ArrayList<>();

        classes.add(objSource.getClass());
        classes.addAll(ClassUtils.getAllInterfaces(objSource.getClass()));
        classes.addAll(ClassUtils.getAllSuperclasses(objSource.getClass()));

        return classes.stream()
                .filter(aClass ->
                        Stream.concat(
                                        Arrays.stream(aClass.getInterfaces()),
                                        Stream.of(aClass.getSuperclass()))
                                .toList()
                                .contains(superclassOrInterface))
                .findFirst()
                .orElseThrow(() -> NO_SUCH_ELEMENT_EXCEPTION);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    private static <R> Class<R> identifyGenericsClassByType(@NonNull Object objSource, Predicate<Type> predicate) {
        var classe = !(objSource instanceof Class<?>) ? objSource.getClass() : (Class<?>) objSource;

        return (Class<R>) Stream.concat(
                        Stream.of(classe.getGenericSuperclass()),
                        Arrays.stream(classe.getGenericInterfaces()))
                .filter(type -> type instanceof ParameterizedType)
                .map(type -> Arrays.stream(((ParameterizedType) type).getActualTypeArguments())
                        .filter(predicate)
                        .findFirst())
                .filter(Optional::isPresent)
                .findFirst()
                .orElseThrow(() -> NO_SUCH_ELEMENT_EXCEPTION)
                .orElse(null);
    }
}