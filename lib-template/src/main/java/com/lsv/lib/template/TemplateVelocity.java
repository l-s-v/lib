package com.lsv.lib.template;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * Para execução de templates feitos em Velocity.
 *
 * @see <a href="https://velocity.apache.org">Velocity</a>
 * @see <a href="https://velocity.apache.org/engine/2.3/vtl-reference.html">Velocity Language Reference</a>
 * @author Leandro da Silva Vieira
 */
@Setter(AccessLevel.PRIVATE)
@Accessors(fluent = true)
class TemplateVelocity extends TemplateAbstract {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Getter(value=AccessLevel.PRIVATE, lazy = true)
    private final VelocityEngine velocityEngine = this.montarVelocityEngine();
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public String aplicarDadosTemplate(String template, Map<String, Object> dados) {
        if(template == null) {
            return null;
        }
        StringWriter writer = new StringWriter();
        // Processa a expressão
        this.velocityEngine().evaluate(new VelocityContext(this.montarContexto(dados)), writer, "erro", template);

        return writer.toString();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private VelocityEngine montarVelocityEngine() {
        // Configura uma instância do velocity para gerar os arquivos
        Properties propriedades = new Properties();
        propriedades.put(VelocityEngine.FILE_RESOURCE_LOADER_PATH, "");
        propriedades.put(VelocityEngine.FILE_RESOURCE_LOADER_CACHE, "true");
        propriedades.put(VelocityEngine.INPUT_ENCODING, ENCODING);

        return new VelocityEngine(propriedades);
    }
}