package com.lsv.lib.template;

import java.util.Map;

/**
 * Define o padrão exigido para executores de template.
 *
 * @author Leandro da Silva Vieira
 */
public interface Template<T> {

    T aplicarDadosTemplate(String template);

    Template<T> adicionarDadoAoContexto(String chave, Object valor);

    Template<T> adicionarDadosAoContexto(Map<String, Object> dados);

    String nome();
}
