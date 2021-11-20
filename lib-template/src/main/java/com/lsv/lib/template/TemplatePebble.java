package com.lsv.lib.template;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Para execução de templates feitos em Pebble.
 *
 * @see <a href="https://pebbletemplates.io/">Pebble</a>
 * @author Leandro da Silva Vieira
 */
@Setter(AccessLevel.PRIVATE)
@Accessors(fluent = true)
public class TemplatePebble implements Template {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Getter(value=AccessLevel.PRIVATE, lazy = true)
    private final PebbleEngine pebbleEngine = this.montarPebbleEngine();
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
                .getTemplate(this.criarArquivoDeTemplateTemporario(template))
                .evaluate(writer, TemplateUtil.montarContexto(dados));

        return writer.toString();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private PebbleEngine montarPebbleEngine() {
        return new PebbleEngine.Builder()
                                .build();
    }

    @SneakyThrows
    private String criarArquivoDeTemplateTemporario(String conteudo){
        Path arquivoTemplate = Paths.get(System.getProperty("java.io.tmpdir"), "template.txt");
        arquivoTemplate.toFile().deleteOnExit();

        Files.deleteIfExists(arquivoTemplate);

        Files.writeString(arquivoTemplate, conteudo);

        return arquivoTemplate.toFile().getPath();
    }
}