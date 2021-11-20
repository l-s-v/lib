package com.lsv.lib.template;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
class TemplateTest {

    @Test
    void aplicarDadosTemplate() {
        Arrays.stream(TemplateTipo.values()).forEach(template ->
                Assertions.assertEquals("Hello Leandro!",
                        template
                        .adicionarDadoAoContexto("nome", "Leandro")
                        .aplicarDadosTemplate(this.carregarArquivo("/templates/" + template.nome()))
                )
        );
    }

    @Test
    void aplicarDadosTemplateSobrepondo() {
        Arrays.stream(TemplateTipo.values()).forEach(template ->
                Assertions.assertEquals("Hello João!",
                        template
                        .adicionarDadoAoContexto("nome", "Leandro")
                        .adicionarDadosAoContexto(Map.of("nome", "Maria"))
                        .aplicarDadosTemplate(
                                this.carregarArquivo("/templates/" + template.nome()),
                                Map.of("nome", "João"))
                )
        );
    }

    @Test
    void aplicarDadosTemplateNulo() {
        Arrays.stream(TemplateTipo.values()).forEach(template ->
                Assertions.assertNull(template.aplicarDadosTemplate(null))
        );
    }

    @Test
    void aplicarDadosTemplateDadoNuloVazio() {
        Arrays.stream(TemplateTipo.values()).forEach(template ->
                Assertions.assertEquals("teste",
                        template
                        .adicionarDadosAoContexto(Map.of())
                        .aplicarDadosTemplate("teste", null)
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
