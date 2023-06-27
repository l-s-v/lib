package com.lsv.lib.core.template;

import lombok.NonNull;

import java.util.Map;

/**
 * Define o padrão exigido para executores de template.
 *
 * @author Leandro da Silva Vieira
 */
public interface Template<T> {

    T aplicarDadosTemplate(@NonNull String template);

    Template<T> adicionarDadoAoContexto(@NonNull String chave, @NonNull Object valor);

    Template<T> adicionarDadosAoContexto(@NonNull Map<String, Object> dados);

    String nome();
}
