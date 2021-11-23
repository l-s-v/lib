package com.lsv.lib.template.mock;

import com.lsv.lib.core.RegisterInterface;
import com.lsv.lib.template.Template;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Stream;

public class TemplateTest {

    @Test
    public void adicionarDadoContexto() {
        templatesParaTestar().forEach(template ->
            Assertions.assertEquals("Hello Leandro!",
                    template
                            .adicionarDadoAoContexto("nome", "Leandro")
                            .aplicarDadosTemplate("Hello nome!")
            )
        );
    }

    @Test
    public void adicionarDadosContexto() {
        templatesParaTestar().forEach(template ->
            Assertions.assertEquals("Hello Maria Silva!",
                    template
                            .adicionarDadosAoContexto(Map.of(
                                            "nome", "João",
                                            "final", "Silva"
                                    )
                            )
                            .adicionarDadosAoContexto(Map.of("nome", "Maria"))
                            .aplicarDadosTemplate("Hello nome final!")
            )
        );
    }

    @Test
    public void adicionarNullContexto() {
        templatesParaTestar().forEach(template ->
            Assertions.assertEquals("Teste",
                template
                    .adicionarDadosAoContexto(null)
                    .aplicarDadosTemplate("Teste")
            )
        );
    }

    @Test
    public void nomeTemplateCorreto() {
        Assertions.assertEquals("SimulaMock".toLowerCase(), new TemplateSimulaMock().nome());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private Stream<Template> templatesParaTestar() {
        return RegisterInterface.create(Template.class)
                .registrar(new TemplateSimulaMock())
                .implementacoes()
                .stream();
    }
}