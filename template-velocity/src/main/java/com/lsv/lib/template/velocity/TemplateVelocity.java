package com.lsv.lib.template.velocity;

import com.google.auto.service.AutoService;
import com.lsv.lib.core.template.Template;
import com.lsv.lib.core.template.TemplateAbstract;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Properties;

/**
 * Para execução de templates feitos em Velocity.
 *
 * @author Leandro da Silva Vieira
 * @see <a href="https://velocity.apache.org">Velocity</a>
 * @see <a href="https://velocity.apache.org/engine/2.3/vtl-reference.html">Velocity Language Reference</a>
 */
@Getter(AccessLevel.PRIVATE)
@AutoService(Template.class)
public class TemplateVelocity extends TemplateAbstract<String> {

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
    public String aplicarDadosTemplate(@NonNull String template) {
        StringWriter writer = new StringWriter();
        // Processa a expressão
        this.velocityEngine().evaluate(new VelocityContext(this.montarContexto()), writer, "erro", template);

        return writer.toString();
    }
}