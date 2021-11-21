package com.lsv.lib.template;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Aplicação do pattern Registry.
 *
 * @author Leandro da Silva Vieira
 */
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TemplateRegister {

    private static final Map<String, Template> templatesRegistrados = new LinkedHashMap<>();

    public static void registrarTemplate(Template template) {
        templatesRegistrados.put(template.nome(), template);
    }

    public static Collection<Template> templatesRegistrados() {
        return templatesRegistrados.values();
    }
}