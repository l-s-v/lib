package com.lsv.lib.template.velocity;

import com.lsv.lib.template.TemplateAbstract;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Properties;

/**
 * Para execução de templates feitos em Velocity.
 *
 * @see <a href="https://velocity.apache.org">Velocity</a>
 * @see <a href="https://velocity.apache.org/engine/2.3/vtl-reference.html">Velocity Language Reference</a>
 * @author Leandro da Silva Vieira
 */
@Getter(AccessLevel.PRIVATE)
@Accessors(fluent = true)
public class TemplateVelocity extends TemplateAbstract<String> {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private final VelocityEngine velocityEngine;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public TemplateVelocity() {
        Properties propriedades = new Properties();
        propriedades.put(VelocityEngine.FILE_RESOURCE_LOADER_PATH, "");
        propriedades.put(VelocityEngine.FILE_RESOURCE_LOADER_CACHE, "true");

        this.velocityEngine = new VelocityEngine(propriedades);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public String aplicarDadosTemplate(String template) {
        if(template == null) {
            return null;
        }
        StringWriter writer = new StringWriter();
        // Processa a expressão
        this.velocityEngine().evaluate(new VelocityContext(this.montarContexto()), writer, "erro", template);

        return writer.toString();
    }
}