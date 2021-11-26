package com.lsv.lib.core.pattern.register;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ServiceLoader;

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
        // Informa ao módulo atual para utilizar a interface
        this.getClass().getModule().addUses(this.classeInterface());
        ServiceLoader.load(this.classeInterface()).stream().forEach(provider -> this.register(provider.get()));
        return this;
    }

    public RegisterInterface<T> register(T template) {
        implementacoes.put(template.getClass().getName(), template);
        return this;
    }

    public Collection<T> subTypes() {
        return implementacoes.values();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <T> RegisterInterface<T> of(Class<T> classeInterface) {
        if(!classeInterface.isInterface()) {
            throw new IllegalArgumentException("Invalid argument. Accepts only interface.");
        }
        return new RegisterInterface<>(classeInterface);
    }
}