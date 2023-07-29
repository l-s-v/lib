package com.lsv.lib.security.web.properties;

import lombok.Data;

import java.util.List;

/**
 * CORS settings.
 *
 * https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html
 *
 * @author Leandro da Silva Vieira
 */
@Data
public class Cors {

    /**
     * Path matcher to which this configuration entry applies
     */
    private String path;

    /**
     * Allowed Origins
     */
    private List<String> allowedOrigins;

    /**
     * Default is "*" which allows all methods
     */
    private List<String> allowedMethods;

    /**
     * Default is "*" which allows all headers
     */
    private List<String> allowedHeaders;

    /**
     * Default is "*" which exposes all headers
     */
    private List<String> exposedHeaders;
}