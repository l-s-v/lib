package com.lsv.lib.template.mock;

import com.lsv.lib.template.TemplateAbstract;

import java.util.Map;

/**
 * Simula uma instância de TemplateAbstract para que a cobertura de código possa analisar as classes criadas.
 */
public class TemplateSimulaMock extends TemplateAbstract<String> {

    @Override
    public String aplicarDadosTemplate(String template) {
        Map<String, Object> dados = this.montarContexto();

        for (String chave : dados.keySet()) {
            template = template.replaceAll(chave, dados.get(chave).toString());
        }

        return template;
    }
}