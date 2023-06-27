package com.lsv.lib.spring.core.message;

import com.lsv.lib.core.message.MessageSource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Leandro da Silva Vieira
 */
@Getter(AccessLevel.PRIVATE)
@AllArgsConstructor

@Component
public class SpringMessageSource implements MessageSource {

    private org.springframework.context.MessageSource messageSource;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public String message(String code, String defaultMessage, Locale locale, Object... args) {
        if (locale == null) {
            locale = LocaleContextHolder.getLocale();
        }

        return messageSource().getMessage(code, args, defaultMessage, locale);
    }
}