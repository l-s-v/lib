package com.lsv.lib.core.mapper;

import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.loader.Loader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Leandro da Silva Vieira
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HelperMappable {

    private static MappableFactory mappableFactory;
    private static Map<String, Mappable<?, ?>> mappablesCache;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static Mappable<?, ?> of(Object sourceBase, @NonNull Class<?> sourceExtends, @NonNull Class<?> destinationExtends) {
        return of(
                HelperClass.identifyGenericsClass(sourceBase, sourceExtends),
                HelperClass.identifyGenericsClass(sourceBase, destinationExtends)
        );
    }

    public static <S, D> Mappable<S, D> of(@NonNull Class<S> sourceClass, @NonNull Class<D> destinationClass) {
        var mappable = findByCache(sourceClass, destinationClass);
        if (mappable == null) {
            mappable = findByMappableFactory(sourceClass, destinationClass);
        }

        if (mappable == null) {
            throw new NoSuchElementException("Não foi encontrada uma classe para o mapeamento entre {%s} e {%s}."
                    .formatted(sourceClass.getName(), destinationClass.getName()));
        }

        return mappable;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    private static <S, D> Mappable<S, D> findByCache(Class<S> sourceClass, Class<D> destinationClass) {
        return (Mappable<S, D>) findMappablesForCache().get(createCacheKey(sourceClass, destinationClass));
    }

    private static Map<String, Mappable<?, ?>> findMappablesForCache() {
        if (mappablesCache == null) {
            mappablesCache = new HashMap<>();

            Loader.of(Mappable.class)
                    .findImplementationsByAllLoaders().stream()
                    .forEach(HelperMappable::addMappableCacheByGenerics);
        }

        return mappablesCache;
    }

    private static <S, D> Mappable<S, D> findByMappableFactory(Class<S> sourceClass, Class<D> destinationClass) {
        if (mappableFactory == null) {
            mappableFactory = Loader.of(MappableFactory.class).findUniqueImplementationByFirstLoader();
        }

        var mappable = mappableFactory.create(sourceClass, destinationClass);
        addMappableCache(mappable, sourceClass, destinationClass);

        return mappable;
    }

    private static void addMappableCacheByGenerics(Mappable<?, ?> mappable) {
        var classe = HelperClass.findDirectSuperclassOrInterface(mappable, Mappable.class);

        Class<?> sourceClass = HelperClass.identifyGenericsClass(classe, 0);
        Class<?> destinationClass = HelperClass.identifyGenericsClass(classe, 1);

        addMappableCache(mappable, sourceClass, destinationClass);
    }

    private static void addMappableCache(Mappable<?, ?> mappable, Class<?> sourceClass, Class<?> destinationClass) {
        String key = createCacheKey(sourceClass, destinationClass);

        var otherMappable = mappablesCache.get(key);
        if (otherMappable != null) {
            throw new IllegalStateException("""
                    Ambiguidade no mapeamento. \
                    Não é possível registrar a classe {%s} para o mapeamento entre {%s} e {%s}. \
                    Conflito com a classe {%s}.""".formatted(
                    mappable.getClass().getName(),
                    sourceClass.getName(),
                    destinationClass.getName(),
                    otherMappable.getClass().getName()));
        }

        mappablesCache.put(key, mappable);
    }

    private static String createCacheKey(Class<?> sourceClass, Class<?> destinationClass) {
        return sourceClass.getName() + "-" + destinationClass.getName();
    }
}