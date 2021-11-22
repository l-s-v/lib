package com.lsv.lib.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TemplateTest {

    @Test
    public void adicionarDadoContexto() {
        Assertions.assertEquals("Hello Leandro!",
                new TemplateSimulaImplementacaoTest()
                        .adicionarDadoAoContexto("nome", "Leandro")
                        .aplicarDadosTemplate("Hello nome!")
        );
    }

    @Test
    public void adicionarDadosContexto() {
        Assertions.assertEquals("Hello Maria Silva!",
            new TemplateSimulaImplementacaoTest()
                .adicionarDadosAoContexto(Map.of(
                                                "nome", "João",
                                                "final", "Silva"
                        )
                )
                .adicionarDadosAoContexto(Map.of("nome", "Maria"))
                .aplicarDadosTemplate("Hello nome final!")
        );
    }

    @Test
    public void adicionarNullContexto() {
        Assertions.assertEquals("Teste",
            new TemplateSimulaImplementacaoTest()
                .adicionarDadosAoContexto(null)
                .aplicarDadosTemplate("Teste")
        );
    }

    @Test
    public void nomeTemplateCorreto() {
        Assertions.assertEquals("simulaimplementacaotest", new TemplateSimulaImplementacaoTest().nome());
    }

    @Test
    public void registroDeTemplate() {
        TemplateRegister.registrarTemplate(new TemplateSimulaImplementacaoTest());
        Assertions.assertEquals(1, TemplateRegister.templatesRegistrados().size());
    }

    @Test
    public void registroDeTemplateNull() {
        Assertions.assertThrowsExactly(NullPointerException.class, () -> TemplateRegister.registrarTemplate(null));
    }
}

/**
 * Simula uma instância de TemplateAbstract para que a cobertura de código possa analisar as classes criadas.
 */
class TemplateSimulaImplementacaoTest extends TemplateAbstract {

    @Override
    public String aplicarDadosTemplate(String template) {
        Map<String, Object> dados = this.montarContexto();

        for (String chave : dados.keySet()) {
            template = template.replaceAll(chave, dados.get(chave).toString());
        }

        return template;
    }
}