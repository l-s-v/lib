package com.lsv.lib.template;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

@Slf4j
class TemplateTest {

    @Test
    void aplicarDadosTemplate() {
        Arrays.stream(TemplateTipo.values()).forEach(template ->
                Assertions.assertEquals("Hello Leandro!",
                        template
                        .adicionarDadoAoContexto("nome", "Leandro")
                        .aplicarDadosTemplate(this.carregarArquivo("/templates/" + template.name()))
                )
        );
    }

    @Test
    void aplicarDadosTemplateSobrepondo() {
        Arrays.stream(TemplateTipo.values()).forEach(template ->
                Assertions.assertEquals("Hello João!",
                        template
                        .adicionarDadoAoContexto("nome", "Leandro")
                        .aplicarDadosTemplate(
                                this.carregarArquivo("/templates/" + template.name()),
                                Map.of("nome", "João"))
                )
        );
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SneakyThrows
    private String carregarArquivo(String nomeArquivo) {
        return StringUtils.join(
                Files.readAllLines(
                        Paths.get(this.getClass().getResource(nomeArquivo).toURI())
                ),
                "\r\n"
        );
    }
}
