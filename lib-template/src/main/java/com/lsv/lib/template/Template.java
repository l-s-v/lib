package com.lsv.lib.template;

import java.util.Map;

/**
 * Define o padrão exigido para executores de template.
 *
 * @author Leandro da Silva Vieira
 */
public interface Template {

    String aplicarDadosTemplate(String template);

    Template adicionarDadoAoContexto(String chave, Object valor);

    Template adicionarDadosAoContexto(Map<String, Object> dados);

    String nome();
}
