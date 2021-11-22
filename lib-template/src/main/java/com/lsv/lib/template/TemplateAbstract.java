package com.lsv.lib.template;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Fornece uma execução básica para o funcionamento do template.
 *
 * @author Leandro da Silva Vieira
 */
@Accessors(fluent = true)
@Getter(AccessLevel.PRIVATE)
public abstract class TemplateAbstract implements Template {

    @SuppressWarnings("all")
    private static final Map<String, Object> DADOS_PADRAO_PARA_CONTEXTO = Map.of(
                                                "StringUtils", new StringUtils()
    );

    private final Map<String, Object> dadosContexto = new LinkedHashMap<>();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public String nome() {
        return this.getClass().getSimpleName().replaceAll(Template.class.getSimpleName(), "").toLowerCase(Locale.ROOT);
    }

    @Override
    public Template adicionarDadoAoContexto(String chave, Object valor) {
        this.dadosContexto().put(chave, valor);
        return this;
    }

    @Override
    public Template adicionarDadosAoContexto(Map<String, Object> dados) {
        this.adicionarDados(dadosContexto, dados);
        return this;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    protected Map<String, Object> montarContexto() {
        Map<String, Object> dadosCompletos = new LinkedHashMap<>(DADOS_PADRAO_PARA_CONTEXTO);
        return this.adicionarDados(dadosCompletos, this.dadosContexto());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private Map<String, Object> adicionarDados(Map<String, Object> dadosFonte, Map<String, Object> dadosNovos) {
        Optional.ofNullable(dadosNovos).ifPresent(dadosFonte::putAll);
        return dadosFonte;
    }
}
