package com.lsv.lib.core.exception;

import lombok.*;

import java.time.Instant;
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
public class ProblemDetail {

// Most common Http status codes
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;

    private static final String PROPERTY_TIMESTAMP = "timestamp";
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private String title;
    private String detail;
    private Object[] detailMessageArguments;
    private int status = BAD_REQUEST;
    @Setter(AccessLevel.PRIVATE)
    private Map<String, Object> properties;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public ProblemDetail() {
        property(PROPERTY_TIMESTAMP, Instant.now());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public ProblemDetail property(String name, @NonNull Object value) {
        if (this.properties == null) {
            this.properties = new LinkedHashMap();
        }

        this.properties.put(name, value);

        return this;
    }
}
