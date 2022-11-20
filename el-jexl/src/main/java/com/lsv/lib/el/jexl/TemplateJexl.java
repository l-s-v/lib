package com.lsv.lib.el.jexl;

import com.google.auto.service.AutoService;
import com.lsv.lib.template.Template;
import com.lsv.lib.template.TemplateAbstract;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;

/**
 * Para execução de expressões regulares (uma forma de template) feitos em Jexl3.
 *
 * @author Leandro da Silva Vieira
 * @see <a href="hhttps://commons.apache.org/proper/commons-jexl/">Jexl3</a>
 */
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@AutoService(Template.class)
public class TemplateJexl extends TemplateAbstract<Object> {

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private JexlEngine engine;
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public TemplateJexl() {
        this.engine(new JexlBuilder().silent(true).create());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public Object aplicarDadosTemplate(@NonNull String template) {
        // Processa a expressão
        return this.engine()
                .createExpression(template)
                .evaluate(new MapContext(this.montarContexto()));
    }
}