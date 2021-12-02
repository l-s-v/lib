package com.lsv.lib.template;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Fornece uma execução básica para o funcionamento do template.
 *
 * @author Leandro da Silva Vieira
 */
@Getter(AccessLevel.PRIVATE)
@Accessors(fluent = true)
public abstract class TemplateAbstract<T> implements Template<T> {

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
    public Template<T> adicionarDadoAoContexto(@NonNull String chave, @NonNull Object valor) {
        this.dadosContexto().put(chave, valor);
        return this;
    }

    @Override
    public Template<T> adicionarDadosAoContexto(@NonNull Map<String, Object> dados) {
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
        dadosFonte.putAll(dadosNovos);
        return dadosFonte;
    }
}
