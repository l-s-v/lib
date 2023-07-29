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

    public static final String CLAIM_TOKEN_ISS = "iss";
    public static final String PARAM_ID_TOKEN_HINT = "id_token_hint";

}