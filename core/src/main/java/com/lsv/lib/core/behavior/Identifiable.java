package com.lsv.lib.core.behavior;

import java.io.Serializable;

/**
 * Define o padrão para domínios identificáveis.
 *
 * @author Leandro da Silva Vieira
 */
public interface Identifiable<ID extends Serializable> extends Serializable {

    /**
     * Deve retorno o objeto que identifica a classe.
     */
    ID id();
}