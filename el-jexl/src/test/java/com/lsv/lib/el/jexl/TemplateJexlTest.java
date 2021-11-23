package com.lsv.lib.el.jexl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TemplateJexlTest {
    
    @Test
    public void aplicarDadosTemplateSobrepondo() {
        String expressao = "((G1 + G2 + G3) * 0.1) + G4";

        Assertions.assertEquals("4.6",
            new TemplateJexl()
                .adicionarDadoAoContexto("G1", 1)
                .adicionarDadoAoContexto("G2", 2)
                .adicionarDadosAoContexto(Map.of(
                                            "G3", 3,
                                            "G4", 4))
                .aplicarDadosTemplate(expressao).toString()
        );
    }

    @Test
    public void aplicarDadosTemplateNulo() {
        Assertions.assertNull(new TemplateJexl().aplicarDadosTemplate(null));
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
}