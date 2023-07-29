package com.lsv.lib.security.web.properties.oidc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Allows greater flexibility for configuring how roles will be read from the token.
 *
 * @author Leandro da Silva Vieira
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "path")
public class Authority {

    public static final String DEFAULT_PREFIX = "ROLE_%s";
    public static final String DEFAULT_PATH_REALM_ACCESS = "$.realm_access.roles";
    public static final String DEFAULT_PATH_RESOURCE_ACESS = "$.resource_access.*.roles";

    public static final Authority REALM_ACCESS_OAUTH2 = new Authority().setPath(DEFAULT_PATH_REALM_ACCESS);
    public static final Authority RESOURCE_ACCESS_OAUTH2 = new Authority().setPath(DEFAULT_PATH_RESOURCE_ACESS);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Pattern (https://jsonpath.com) to retrieve value inside json.
     */
    @NotBlank
    private String path;

    /**
     * Pattern that must be applied to the authority.
     * Default is ROLE_%s.
     */
    @NotBlank
    private String pattern = DEFAULT_PREFIX;

    /**
     * Text transformation that should be applied: UNCHANGED, UPPER, LOWER.
     * Defaault is UPPER.
     */
    @NotNull
    private Transform transform = Transform.UPPER;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public enum Transform {
        UNCHANGED,
        UPPER,
        LOWER;
    }
}