package com.lsv.lib.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Map;

@Slf4j
class TemplateTest {

    @Test
    void adicionarDadoContexto() {
        Assertions.assertEquals("Hello Leandro!",
                new TemplateSimulaImplementacaoTest()
                        .adicionarDadoAoContexto("nome", "Leandro")
                        .aplicarDadosTemplate("Hello nome!")
        );
    }

    @Test
    void adicionarDadosContexto() {
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
    void adicionarNullContexto() {
        Assertions.assertEquals("Teste",
            new TemplateSimulaImplementacaoTest()
                .adicionarDadosAoContexto(null)
                .aplicarDadosTemplate("Teste")
        );
    }

    @Test
    void nomeTemplateCorreto() {
        Assertions.assertEquals("simulaimplementacaotest", new TemplateSimulaImplementacaoTest().nome());
    }

    @Test
    void registroDeTemplate() {
        TemplateRegister.registrarTemplate(new TemplateSimulaImplementacaoTest());
        Assertions.assertEquals(1, TemplateRegister.templatesRegistrados().size());
    }

    @Test
    void registroDeTemplateNull() {
        Assertions.assertThrowsExactly(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                TemplateRegister.registrarTemplate(null);
            }
        });
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