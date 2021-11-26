package com.lsv.lib.template.pebble;

import com.lsv.lib.template.TemplateAbstract;
import com.mitchellbosecke.pebble.PebbleEngine;
import lombok.*;

import java.io.StringWriter;

/**
 * Para execução de templates feitos em Pebble.
 *
 * @see <a href="https://pebbletemplates.io/">Pebble</a>
 * @author Leandro da Silva Vieira
 */
@Setter(AccessLevel.PRIVATE)
@Getter(value=AccessLevel.PRIVATE)
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
    public String aplicarDadosTemplate(@NonNull String template) {
        StringWriter writer = new StringWriter();
        // Processa a expressão
        this.pebbleEngine()
                .getLiteralTemplate(template)
                .evaluate(writer, this.montarContexto());

        return writer.toString();
    }
}