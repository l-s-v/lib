package com.lsv.lib.template;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;

/**
 * Tipos de template.
 *
 * @author Leandro da Silva Vieira
 */
@Accessors(fluent = true)
@AllArgsConstructor
public enum TemplateTipo implements Template {
        VELOCITY("velocity", new TemplateVelocity()),
        PEBBLE("pebble", new TemplatePebble()),
    ;

    @Getter
    private String nome;

    @Delegate(types = {Template.class})
    private Template template;
}
