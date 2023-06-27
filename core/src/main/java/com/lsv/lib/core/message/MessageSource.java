package com.lsv.lib.core.message;

import java.util.Locale;

/**
 * @author Leandro da Silva Vieira
 */
public interface MessageSource {

    String message(String code, String defaultMessage, Locale locale, Object ... args);

    default String message(String code, Locale locale, Object[] args) {
        return message(code, null, locale, args);
    }

    default String message(String code, Object ... args) {
        return message(code, null, args);
    }
}