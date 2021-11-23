package com.lsv.lib.template.pebble;

import com.lsv.lib.template.TemplateAbstract;
import com.mitchellbosecke.pebble.PebbleEngine;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.io.StringWriter;

/**
 * Para execução de templates feitos em Pebble.
 *
 * @see <a href="https://pebbletemplates.io/">Pebble</a>
 * @author Leandro da Silva Vieira
 */
@Setter(AccessLevel.PRIVATE)
@Getter(value=AccessLevel.PRIVATE)
@Accessors(fluent = true)
public class TemplatePebble extends TemplateAbstract<String> {

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private final PebbleEngine pebbleEngine;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public TemplatePebble() {
        pebbleEngine = new PebbleEngine.Builder().build();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    // Anotação não coberta em cobertura de testes, mas o método evaluate
    // nunca lançará IOException visto que não está utilizando arquivo.
    @SneakyThrows
    @Override
    public String aplicarDadosTemplate(String template) {
        if(template == null) {
            return null;
        }

        StringWriter writer = new StringWriter();
        // Processa a expressão
        this.pebbleEngine()
                .getLiteralTemplate(template)
                .evaluate(writer, this.montarContexto());

        return writer.toString();
    }
}