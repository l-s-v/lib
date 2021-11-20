package com.lsv.lib.template;

import java.util.Map;

/**
 * Define o padrão exigido para executores de template.
 *
 * @author Leandro da Silva Vieira
 */
public interface Template {

    String aplicarDadosTemplate(String template, Map<String, Object> dados);

    Template adicionarDadoAoContexto(String chave, Object valor);

    Template adicionarDadosAoContexto(Map<String, Object> dados);

    default String aplicarDadosTemplate(String template) {
        return this.aplicarDadosTemplate(template, null);
    }
}
