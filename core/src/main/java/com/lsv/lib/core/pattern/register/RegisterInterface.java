package com.lsv.lib.core.pattern.register;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;

/**
 * Aplicação do pattern Registry para qualquer Interface.
 *
 * @author Leandro da Silva Vieira
 */
public final class RegisterInterface<T> {

    private final Map<String, T> implementacoes = new LinkedHashMap<>();
    @Getter(AccessLevel.PRIVATE)
    private final Class<T> classeInterface;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private RegisterInterface(Class<T> classeInterface) {
        this.classeInterface = classeInterface;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public RegisterInterface<T> findSubtypes() {
        // Informa que o módulo atual utiliza a interface
        this.getClass().getModule().addUses(this.classeInterface());
        ServiceLoader.load(this.classeInterface()).stream().forEach(provider -> this.register(provider.get()));
        return this;
    }

    public RegisterInterface<T> register(T template) {
        implementacoes.put(template.getClass().getName(), template);
        return this;
    }

    public List<T> implementations() {
        return List.copyOf(implementacoes.values());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <T> RegisterInterface<T> of(@NonNull Class<T> classeInterface) {
        if (!classeInterface.isInterface()) {
            throw new IllegalArgumentException("Invalid argument. Accepts only interface.");
        }
        return new RegisterInterface<>(classeInterface);
    }

    public static <T> T findImplementation(@NonNull Class<T> classeInterface) {
        List<T> subTypes = RegisterInterface.of(classeInterface)
            .findSubtypes()
            .implementations();

        if(subTypes.isEmpty()) {
            throw new NoSuchElementException("No implementation found to interface " + classeInterface.getName());
        }
        if(subTypes.size() > 1) {
            throw new IllegalArgumentException("Found more than one implementation to interface " + classeInterface.getName());
        }

        return subTypes.get(0);
    }
}