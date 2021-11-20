package com.lsv.lib.template;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Slf4j
class TemplateVelocityTest {

    @SneakyThrows
    @Test
    void aplicarDadosTemplateTest(){
        String template = StringUtils.join(
                                Files.readAllLines(
                                    Paths.get(this.getClass().getResource("/templates/velocity/teste.vm").toURI())
                                ),
                                "\r\n"
        );

        template = new TemplateVelocity().aplicarDadosTemplate(template, Map.of("nome", "Leandro"));

        Assertions.assertEquals("Hello Leandro!", template);
    }
}
