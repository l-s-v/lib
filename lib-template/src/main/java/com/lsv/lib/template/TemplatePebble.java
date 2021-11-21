package com.lsv.lib.template;

import com.mitchellbosecke.pebble.PebbleEngine;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.io.StringWriter;
import java.util.Map;

/**
 * Para execução de templates feitos em Pebble.
 *
 * @see <a href="https://pebbletemplates.io/">Pebble</a>
 * @author Leandro da Silva Vieira
 */
@Setter(AccessLevel.PRIVATE)
@Getter(value=AccessLevel.PRIVATE)
@Accessors(fluent = true)
class TemplatePebble extends TemplateAbstract {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private final PebbleEngine pebbleEngine;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public TemplatePebble() {
        pebbleEngine = new PebbleEngine.Builder().build();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SneakyThrows
    @Override
    public String aplicarDadosTemplate(String template, Map<String, Object> dados) {
        if(template == null) {
            return null;
        }

        StringWriter writer = new StringWriter();
        // Processa a expressão
        this.pebbleEngine()
                .getLiteralTemplate(template)
                .evaluate(writer, this.montarContexto(dados));

        return writer.toString();
    }
}