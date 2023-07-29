package com.lsv.lib.security.web.properties.oidc;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Extra settings for the issuer.
 * Grouped to not extend the main class too much with features that most of the time should use the default values.
 *
 * @author Leandro da Silva Vieira
 */
@Data
public class ExtraConfig {

    /**
     * Amount of time, in seconds, specifying the minimum interval between two requests to retrieve new public keys.
     * By default it will be 10 seconds. This is to avoid DoS when attacker sends lots of tokens with bad kid
     * forcing adapter to send lots of requests to Keycloak.
     */
    @NotNull
    private long jwksMinTimeBetweenRequests = 10;
    /**
     * Amount of time, in seconds, that jwks keys should be retrieved from the cache rather than
     * downloaded from the server. By default it is 86400 seconds (1 day).
     */
    @NotNull
    private long jwksMaxTimeCached = 86_400;
}