package com.lsv.lib.core.exception.helper;

import com.lsv.lib.core.concept.dto.Dto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Lets you inform properties that comply with RFC7807.
 *
 * @author Leandro da Silva Vieira
 */
@Getter
@Setter
@ToString
@SuperBuilder(builderMethodName = "of", buildMethodName = "get")
public final class ProblemDetail implements Dto {

// Most common Http status codes
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private String title;
    private String detail;
    private Integer status;
    private String instance;
    @Setter(AccessLevel.PRIVATE)
    private Map<String, Object> properties;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public ProblemDetail property(String name, @NonNull Object value) {
        if (this.properties == null) {
            this.properties = new LinkedHashMap();
        }

        this.properties.put(name, value);

        return this;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public Integer getStatus() {
        return status != null ? status : BAD_REQUEST;
    }
}