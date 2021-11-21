package com.lsv.lib.template;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fornece uma execução básica para o funcionamento do template.
 *
 * @author Leandro da Silva Vieira
 */
@Accessors(fluent = true)
@Getter(AccessLevel.PRIVATE)
abstract class TemplateAbstract implements Template {

    @SuppressWarnings("all")
    private static final Map<String, Object> DADOS_PADRAO_PARA_CONTEXTO = Map.of(
                                                "StringUtils", new StringUtils()
    );

    private final Map<String, Object> dadosContexto = new LinkedHashMap<>();
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public Template adicionarDadoAoContexto(String chave, Object valor) {
        this.dadosContexto().put(chave, valor);
        return this;
    }

    @Override
    public Template adicionarDadosAoContexto(Map<String, Object> dados) {
        this.dadosContexto.putAll(dados);
        return this;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    Map<String, Object> montarContexto(Map<String, Object> dadosParaContexto) {
        Map<String, Object> dadosCompletos = new LinkedHashMap<>(DADOS_PADRAO_PARA_CONTEXTO);

        this
            .adicionarDados(dadosCompletos, this.dadosContexto())
            .adicionarDados(dadosCompletos, dadosParaContexto);

        return dadosCompletos;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private TemplateAbstract adicionarDados(Map<String, Object> dadosFonte, Map<String, Object> dadosNovos) {
        if(dadosNovos != null) {
            dadosFonte.putAll(dadosNovos);
        }

        return this;
    }
}
