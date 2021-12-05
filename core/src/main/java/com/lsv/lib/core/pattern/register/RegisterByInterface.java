package com.lsv.lib.core.pattern.register;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Aplicação do pattern Registry para qualquer Interface.
 *
 * @author Leandro da Silva Vieira
 */
public final class RegisterByInterface<T> {

    private final Map<String, T> implementacoes = new LinkedHashMap<>();
    @Getter(AccessLevel.PRIVATE)
    private final Class<T> classeInterface;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private RegisterByInterface(Class<T> classeInterface) {
        this.classeInterface = classeInterface;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public RegisterByInterface<T> findImplementationsByService() {
        // Informa que o módulo atual utiliza a interface
        this.getClass().getModule().addUses(this.classeInterface());
        ServiceLoader.load(this.classeInterface()).stream().forEach(provider -> this.register(provider.get()));
        return this;
    }

    @SuppressWarnings("unchecked")
    public RegisterByInterface<T> findImplementationsByReflection(String packageName) {
        new Reflections(packageName).getSubTypesOf(this.classeInterface())
            .stream().filter(aClass -> !aClass.isInterface())
            .forEach(aClass -> {
                try {
                    this.register((T) aClass.getConstructors()[0].newInstance());
                } catch (InstantiationException |
                    IllegalAccessException |
                    InvocationTargetException |
                    IllegalArgumentException |
                    ArrayIndexOutOfBoundsException e) {
                    throw new UnsupportedOperationException("Could not instantiate class " + aClass, e);
                }
            });
        return this;
    }

    public RegisterByInterface<T> register(T template) {
        implementacoes.put(template.getClass().getName(), template);
        return this;
    }

    public List<T> implementations() {
        return List.copyOf(implementacoes.values());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <T> RegisterByInterface<T> of(@NonNull Class<T> classeInterface) {
        if (!classeInterface.isInterface()) {
            throw new IllegalArgumentException("Invalid argument. Accepts only interface.");
        }
        return new RegisterByInterface<>(classeInterface);
    }

    public static <T> T findImplementation(@NonNull Class<T> classeInterface) {
        List<T> subTypes = RegisterByInterface.of(classeInterface)
            .findImplementationsByService()
            .implementations();

        if (subTypes.isEmpty()) {
            throw new NoSuchElementException("No implementation found to interface " + classeInterface.getName());
        }
        if (subTypes.size() > 1) {
            throw new IllegalArgumentException("Found more than one implementation to interface " + classeInterface.getName());
        }

        return subTypes.get(0);
    }
}