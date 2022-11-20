package com.lsv.lib.core.helper;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
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

    public static <T extends Serializable> T cloneSerializable(@NonNull T obj) {
        return deserealize(serialize(obj));
    }

    @SneakyThrows
    public static <T extends Serializable> byte[] serialize(@NonNull T obj) {
        try (ByteArrayOutputStream bout = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bout)) {

            out.writeObject(obj);

            return bout.toByteArray();
        }
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deserealize(@NonNull byte[] objAsBytes) {
        try (ByteArrayInputStream bin = new ByteArrayInputStream(objAsBytes);
             ObjectInputStream in = new ObjectInputStream(bin)) {

            return (T) in.readObject();
        }
    }
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    private static <R> Class<R> identifyGenericsClassByType(@NonNull Object objSource, Predicate<Type> predicate) {
        return (Class<R>) Stream.concat(
                        Stream.of(objSource.getClass().getGenericSuperclass()),
                        Arrays.stream(objSource.getClass().getGenericInterfaces()))
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