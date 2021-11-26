package com.lsv.lib.template.velocity;

import com.lsv.lib.core.pattern.register.RegisterInterface;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

public class TemplateVelocityTest {

    @Test
    public void aplicarDadosTemplate() {
        templatesParaTestar().forEach(template ->
            Assertions.assertEquals("Hello Leandro!",
                template
                .adicionarDadoAoContexto("nome", "Leandro")
                .aplicarDadosTemplate(this.carregarArquivo("/templates/" + template.nome()))
            )
        );
    }

    @Test
    public void aplicarDadosTemplateSobrepondo() {
        templatesParaTestar().forEach(template ->
            Assertions.assertEquals("Hello Maria!",
                template
                .adicionarDadoAoContexto("nome", "Leandro")
                .adicionarDadosAoContexto(Map.of("nome", "Maria"))
                .aplicarDadosTemplate(this.carregarArquivo("/templates/" + template.nome()))
            )
        );
    }

    @Test
    public void aplicarDadosTemplateNulo() {
        templatesParaTestar().forEach(template ->
            Assertions.assertThrows(NullPointerException.class, () -> template.aplicarDadosTemplate(null))
        );
    }

    @Test
    public void aplicarDadosTemplateDadoNuloVazio() {
        templatesParaTestar().forEach(template ->
            Assertions.assertEquals("teste",
                template
                .adicionarDadosAoContexto(Map.of())
                .aplicarDadosTemplate("teste")
            )
        );
    }

    @Test
    public void aplicarDadosTemplateSobrepondoMultiplosDados() {
        templatesParaTestar().forEach(template ->
            Assertions.assertEquals("Hello Maria!",
                template
                    .adicionarDadosAoContexto(Map.of("nome", "Maria"))
                    .aplicarDadosTemplate(
                        this.carregarArquivo("/templates/" + template.nome()))
            )
        );
    }

    @Test
    public void testandoRegistroTemplate() {
        templatesParaTestar().forEach(template ->
            Assertions.assertEquals("Hello Maria!",
                template
                    .adicionarDadosAoContexto(Map.of("nome", "Maria"))
                    .aplicarDadosTemplate(
                        this.carregarArquivo("/templates/" + template.nome()))
            )
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
    
    private Stream<TemplateVelocity> templatesParaTestar() {
        return RegisterInterface.of(TemplateVelocity.class).findSubtypes().subTypes().stream();
    }
}