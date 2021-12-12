package com.lsv.lib.core.behavior;

import lombok.NonNull;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * Define o padrão para domínios identificáveis.
 *
 * @author Leandro da Silva Vieira
 */
public interface Identifiable<ID extends Serializable> extends Serializable {

    /**
     * Deve retorno o objeto que identifica a classe.
     */
    ID getId();

    <I> I setId(ID id);

    @SuppressWarnings("unchecked")
    static <
        I extends Identifiable<ID>,
        ID extends Serializable>
    I of(@NonNull Class<I> iClass, ID id) {
        try {
            return iClass.getConstructor().newInstance().setId(id);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new UnsupportedOperationException("Could not instantiate class " + iClass, e);
        }
    }
}