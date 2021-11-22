package com.lsv.lib.template.mock;

import com.lsv.lib.template.TemplateRegister;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TemplateTest {

    @Test
    public void adicionarDadoContexto() {
        TemplateRegister.templatesRegistrados().forEach(template ->
            Assertions.assertEquals("Hello Leandro!",
                template
                    .adicionarDadoAoContexto("nome", "Leandro")
                    .aplicarDadosTemplate("Hello nome!")
            )
        );
    }

    @Test
    public void adicionarDadosContexto() {
        TemplateRegister.templatesRegistrados().forEach(template ->
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
        TemplateRegister.templatesRegistrados().forEach(template ->
            Assertions.assertEquals("Teste",
                template
                    .adicionarDadosAoContexto(null)
                    .aplicarDadosTemplate("Teste")
            )
        );
    }

    @Test
    public void nomeTemplateCorreto() {
        TemplateRegister.templatesRegistrados().forEach(template ->
                Assertions.assertEquals("SimulaMock".toLowerCase(), template.nome())
        );
    }

    @Test
    public void registroDeTemplate() {
        TemplateRegister.registrarTemplate(new TemplateSimulaMock());
        Assertions.assertEquals(1, TemplateRegister.templatesRegistrados().size());
    }

    @Test
    public void registroDeTemplateNull() {
        Assertions.assertThrowsExactly(NullPointerException.class, () -> TemplateRegister.registrarTemplate(null));
    }
}