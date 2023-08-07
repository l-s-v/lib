package com.lsv.lib.spring.jpa.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * @author Leandro da Silva Vieira
 */
@Converter
public class SetToStringConverter implements AttributeConverter<Set<Object>, String> {
    private final String SEPARATOR = ";";

    @Override
    public String convertToDatabaseColumn(Set<Object> objs) {
        return objs == null ? null : StringUtils.arrayToDelimitedString(objs.toArray(), SEPARATOR);
    }

    @Override
    public Set<Object> convertToEntityAttribute(String value) {
        return value == null ? null : Set.of(value.split(SEPARATOR));
    }
}