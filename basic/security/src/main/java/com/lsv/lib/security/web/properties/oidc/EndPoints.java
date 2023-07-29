package com.lsv.lib.security.web.properties.oidc;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Settings for an OpenId Connect server (../.well-known/openid-configuration).
 *
 * @author Leandro da Silva Vieira
 */
@Data
public class EndPoints {

    private static final String OPENID_PROTOCOL = "%s/protocol/openid-connect/";

    public static final String OPENID_URI_AUTHORIZATION = OPENID_PROTOCOL + "auth";
    public static final String OPENID_URI_TOKEN = OPENID_PROTOCOL + "token";
    public static final String OPENID_URI_INTROSPECT = OPENID_PROTOCOL + "token/introspect";
    public static final String OPENID_URI_USER_INFO = OPENID_PROTOCOL + "userinfo";
    public static final String OPENID_URI_END_SESSION = OPENID_PROTOCOL + "logout";
    public static final String OPENID_URI_JWKS = OPENID_PROTOCOL + "certs";

    public static final String OPENID_GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
    public static final String OPENID_ESCOPE_OPEN_ID = "openid";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private String issuer;
    @NotBlank
    private String authorizationEndpoint = OPENID_URI_AUTHORIZATION;
    @NotBlank
    private String tokenEndpoint = OPENID_URI_TOKEN;
    @NotBlank
    private String introspectionEndpoint = OPENID_URI_INTROSPECT;
    @NotBlank
    private String userInfoEndpoint = OPENID_URI_USER_INFO;
    @NotBlank
    private String endSessionEndpoint = OPENID_URI_END_SESSION;
    @NotBlank
    private String jwksUri = OPENID_URI_JWKS;
    @NotBlank
    private String grantTypesSupported = OPENID_GRANT_TYPE_AUTHORIZATION_CODE;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public String getAuthorizationEndpoint() {
        return format(authorizationEndpoint);
    }

    public String getTokenEndpoint() {
        return format(tokenEndpoint);
    }

    public String getIntrospectionEndpoint() {
        return format(introspectionEndpoint);
    }

    public String getUserInfoEndpoint() {
        return format(userInfoEndpoint);
    }

    public String getEndSessionEndpoint() {
        return format(endSessionEndpoint);
    }

    public String getJwksUri() {
        return format(jwksUri);
    }

    public String getGrantTypesSupported() {
        return format(grantTypesSupported);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    
    private String format(String value) {
        return value.formatted(getIssuer());
    }
}