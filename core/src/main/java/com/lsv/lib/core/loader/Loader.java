package com.lsv.lib.core.loader;

import com.lsv.lib.core.helper.HelperPriority;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * Procura por implementações de acordo com a classe informada.
 *
 * @author Leandro da Silva Vieira
 */
public final class Loader<T> {

    private static final List<Loadable> loadables = findLoadables();

    @NonNull
    @Getter(AccessLevel.PRIVATE)
    private final Class<T> classType;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private Loader(@NonNull Class<T> classType) {
        if (classNotIsValid(classType)) {
            throw new IllegalArgumentException("Invalid argument. Accepts only interface.");
        }
        this.classType = classType;
    }

    public static <T> Loader<T> of(@NonNull Class<T> classType) {
        return new Loader<>(classType);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public List<T> findImplementationsByAllLoaders() {
        return findImplementationsByService(false);
    }

    public List<T> findImplementationsByFirstLoader() {
        return findImplementationsByService(true);
    }

    public T findUniqueImplementationByFirstLoader() {
        List<T> subTypes = findImplementationsByFirstLoader();

        if (subTypes.isEmpty()) {
            throw new NoSuchElementException("No implementation found to interface " + classType.getName());
        }
        if (subTypes.size() > 1) {
            throw new IllegalArgumentException("Found more than one implementation to interface " + classType.getName());
        }

        return subTypes.get(0);
    }

    public List<T> findImplementationsByReflection(@NonNull String packageName) {
        var implementations = new LinkedList<T>();

        getSubTypesOf(packageName)
                .filter(aClass -> classNotIsValid(aClass) && !aClass.isAnonymousClass())
                .forEach(aClass -> {
                    try {
                        implementations.add(aClass.getConstructor().newInstance());
                    } catch (InstantiationException |
                            IllegalAccessException |
                            InvocationTargetException |
                            IllegalArgumentException |
                            ArrayIndexOutOfBoundsException |
                            NoSuchMethodException e) {
                        throw new UnsupportedOperationException("Could not instantiate class " + aClass, e);
                    }
                });

        return prepareList(implementations);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private List<T> findImplementationsByService(boolean onlyFirstLoadable) {
        var implementations = new LinkedList<T>();

        for (Loadable loadable : loadables) {
            if (onlyFirstLoadable && !implementations.isEmpty()) {
                break;
            }

            loadable.load(classType()).stream().forEach(implementations::add);
        }

        return prepareList(implementations);
    }

    private boolean classNotIsValid(@NonNull Class<?> classType) {
        return !(classType.isInterface() || Modifier.isAbstract(classType.getModifiers()));
    }

    private Stream<Class<? extends T>> getSubTypesOf(String packageName) {
        return new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName)))
                .getSubTypesOf(classType()).stream();
    }

    private List<T> prepareList(List<T> implementations) {
        implementations.sort(HelperPriority::comparePriorityDescending);
        return List.copyOf(implementations);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static List<Loadable> findLoadables() {
        return new SPILoader().load(Loadable.class).stream()
                .sorted(HelperPriority::comparePriorityDescending)
                .toList();
    }
}