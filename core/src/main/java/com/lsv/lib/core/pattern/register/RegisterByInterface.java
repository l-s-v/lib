package com.lsv.lib.core.pattern.register;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

/**
 * Aplicação do pattern Registry para qualquer Interface.
 *
 * @author Leandro da Silva Vieira
 */
public final class RegisterByInterface<T> {

    private final Map<String, T> implementations = new WeakHashMap<>();
    @NonNull
    @Getter(AccessLevel.PRIVATE)
    private final Class<T> classType;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private RegisterByInterface(@NonNull Class<T> classType) {
        if (classNotIsValid(classType)) {
            throw new IllegalArgumentException("Invalid argument. Accepts only interface.");
        }
        this.classType = classType;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public RegisterByInterface<T> findImplementationsByService() {
        // Informa que o módulo atual utiliza a interface
        this.getClass().getModule().addUses(classType());
        ServiceLoader.load(classType()).stream().forEach(provider -> register(provider.get()));
        return this;
    }

    public RegisterByInterface<T> findImplementationsByReflection(@NonNull String packageName) {
        getSubTypesOf(packageName)
            .filter(aClass -> classNotIsValid(aClass) && !aClass.isAnonymousClass())
            .forEach(aClass -> {
                try {
                    register(aClass.getConstructor().newInstance());
                } catch (InstantiationException |
                    IllegalAccessException |
                    InvocationTargetException |
                    IllegalArgumentException |
                    ArrayIndexOutOfBoundsException |
                    NoSuchMethodException e) {
                    throw new UnsupportedOperationException("Could not instantiate class " + aClass, e);
                }
            });
        return this;
    }

    public List<Class<? extends T>> findSubInterfaces(@NonNull String packageName) {
        return getSubTypesOf(packageName)
            .filter(Class::isInterface)
            .toList();
    }

    public RegisterByInterface<T> register(T obj) {
        implementations.put(obj.getClass().getName(), obj);
        return this;
    }

    public List<T> implementations() {
        return List.copyOf(implementations.values());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <T> RegisterByInterface<T> of(@NonNull Class<T> classType) {
        return new RegisterByInterface<>(classType);
    }

    public static <T> T findImplementation(@NonNull Class<T> classType) {
        List<T> subTypes = RegisterByInterface.of(classType)
            .findImplementationsByService()
            .implementations();

        if (subTypes.isEmpty()) {
            throw new NoSuchElementException("No implementation found to interface " + classType.getName());
        }
        if (subTypes.size() > 1) {
            throw new IllegalArgumentException("Found more than one implementation to interface " + classType.getName());
        }

        return subTypes.get(0);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private boolean classNotIsValid(@NonNull Class<?> classType) {
        return !(classType.isInterface() || Modifier.isAbstract(classType.getModifiers()));
    }

    private Stream<Class<? extends T>> getSubTypesOf(String packageName) {
        return new Reflections(new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forPackage(packageName)))
            .getSubTypesOf(classType()).stream();
    }
}