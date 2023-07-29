package com.lsv.lib.spring.web.properties;

import com.lsv.lib.core.properties.LibProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Settings for swagger.
 *
 * @author Leandro da Silva Vieira
 */
@Data

@Validated
@ConfigurationProperties(SwaggerProperties.PATH)
public class SwaggerProperties implements LibProperties {

    public static final String PATH = SpringWebProperties.PATH + ".api-docs";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private String title = "OpenAPI definition";
    private String description;
    private String uriTermsOfService;
    private String version = "v0";
    @NotNull
    private Contact contact = new Contact();

    @Data
    public static class Contact {
        private String name;
        private String url;
        private String email;
    }
}