package com.lsv.lib.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Aplicação do pattern Registry para qualquer Interface.
 *
 * @author Leandro da Silva Vieira
 */
@Accessors(fluent = true)
public final class RegisterInterface <Interface> {

    private final Map<String, Interface> implementacoes = new LinkedHashMap<>();
    @Getter(AccessLevel.PRIVATE)
    private final Class<Interface> classeInterface;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private RegisterInterface(Class<Interface> classeInterface) {
        this.classeInterface = classeInterface;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public RegisterInterface<Interface> pesquisarImplementacoesModularizadas() {
        ServiceLoader.load(this.classeInterface()).stream().forEach(provider -> this.registrar(provider.get()));
        return this;
    }

    public RegisterInterface<Interface> registrar(Interface template) {
        implementacoes.put(template.getClass().getName(), template);
        return this;
    }

    public Collection<Interface> implementacoes() {
        return implementacoes.values();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <T> RegisterInterface<T> create(Class<T> classeInterface) {
        return new RegisterInterface<>(classeInterface);
    }
}