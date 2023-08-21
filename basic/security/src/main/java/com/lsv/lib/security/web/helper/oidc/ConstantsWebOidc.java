package com.lsv.lib.security.web.helper.oidc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Leandro da Silva Vieira
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstantsWebOidc {

    public static final String CLAIM_TOKEN_ISS = "iss"; // issuer
    public static final String CLAIM_TOKEN_AZP = "azp"; // client_id
    public static final String CLAIM_TOKEN_SUB = "sub"; // id into issuer

    public static final String PARAM_ID_TOKEN_HINT = "id_token_hint";

    public static final String ATTR_CLIENT_ID = "client_id";
    public static final String ATTR_CLIENT_SECRET = "client_secret";
    public static final String ATTR_GRANT_TYPE = "grant_type";
    public static final String ATTR_ACCESS_TOKEN = "access_token";

    public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
}