package com.lsv.lib.security.web.properties.oidc;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Data to get an access token from a service account [SAT - service account token].
 *
 * https://datatracker.ietf.org/doc/html/rfc6749#section-1.3.4
 *
 * @author Leandro da Silva Vieira
 */
@Data
@Builder
public class ServiceAccount {

    /**
     * The client identifier issued to the client during the registration process.
     */
    @NotBlank
    private String clientId;
    /**
     * The client secret.
     */
    @NotBlank
    private String clientSecret;
    /**
     * Scopes of the access request.
     *
     * https://datatracker.ietf.org/doc/html/rfc6749#section-3.3
     */
    private List<String> scope;
    /**
     * URI to obtain the token.
     */
    @NotBlank
    private String tokenUri;
}