package com.lsv.lib.template;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fornece procedimentos auxiliares ao template.
 *
 * @author Leandro da Silva Vieira
 */
final class TemplateUtil {

    private static final Map<String, Object> DADOS_PADRAO_CONTEXTO = Map.of(
                                                "StringUtils", new StringUtils()
//                                                "Util", new Util(),
//                                                "Numero", new Numero()
    );

    public static Map<String, Object> montarContexto(Map<String, Object> dadosParaContexto) {
        Map<String, Object> dadosCompletos = new LinkedHashMap<String, Object>(DADOS_PADRAO_CONTEXTO);
        dadosCompletos.putAll(dadosParaContexto);

        return dadosCompletos;
    }
}
