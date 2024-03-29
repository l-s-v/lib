package com.lsv.lib.template.mock;

import com.google.auto.service.AutoService;
import com.lsv.lib.template.Template;
import com.lsv.lib.template.TemplateAbstract;
import lombok.NonNull;

import java.util.Map;

/**
 * Simula uma instância de TemplateAbstract para que a cobertura de código possa analisar as classes criadas.
 */
@AutoService(Template.class)
public class TemplateSimulaMock extends TemplateAbstract<String> {

    @Override
    public String aplicarDadosTemplate(@NonNull String template) {
        Map<String, Object> dados = this.montarContexto();

        for (String chave : dados.keySet()) {
            template = template.replaceAll(chave, dados.get(chave).toString());
        }

        return template;
    }
}