package com.lsv.lib.template;

/**
 * Define o padrão exigido para executores de template.
 */
public interface Template {

    String ENCODING = "UTF-8";

    String aplicarDadosTemplate(String template, java.util.Map<String, Object> dados);
}
