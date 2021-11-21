package com.lsv.lib.template;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Slf4j
class TemplatePebbleTest {

    private final TemplatePebble template = new TemplatePebble();

    @Test
    void aplicarDadosTemplate() {
        Assertions.assertEquals("Hello Leandro!",
            template
            .adicionarDadoAoContexto("nome", "Leandro")
            .aplicarDadosTemplate(this.carregarArquivo("/templates/" + template.nome()))
        );
    }

    @Test
    void aplicarDadosTemplateSobrepondo() {
        Assertions.assertEquals("Hello Maria!",
            template
            .adicionarDadoAoContexto("nome", "Leandro")
            .adicionarDadosAoContexto(Map.of("nome", "Maria"))
            .aplicarDadosTemplate(this.carregarArquivo("/templates/" + template.nome()))
        );
    }

    @Test
    void aplicarDadosTemplateNulo() {
        Assertions.assertNull(template.aplicarDadosTemplate(null));
    }

    @Test
    void aplicarDadosTemplateDadoNuloVazio() {
        Assertions.assertEquals("teste",
            template
            .adicionarDadosAoContexto(Map.of())
            .aplicarDadosTemplate("teste")
        );
    }

    @Test
    void aplicarDadosTemplateSobrepondoMultiplosDados() {
        Assertions.assertEquals("Hello Maria!",
            template
                .adicionarDadosAoContexto(Map.of("nome", "Maria"))
                .aplicarDadosTemplate(
                    this.carregarArquivo("/templates/" + template.nome()))
        );
    }

    @Test
    void testandoRegistroTemplate() {
        Assertions.assertEquals("Hello Maria!",
            template
                .adicionarDadosAoContexto(Map.of("nome", "Maria"))
                .aplicarDadosTemplate(
                    this.carregarArquivo("/templates/" + template.nome()))
        );
    }
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SneakyThrows
    private String carregarArquivo(String nomeArquivo) {
        URL url = this.getClass().getResource(nomeArquivo);
        if (url != null) {
            return StringUtils.join(Files.readAllLines(Paths.get(url.toURI())), "\r\n");
        }
        return null;
    }
}
