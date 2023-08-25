package com.lsv.lib.spring.web.client.helper;

import com.google.common.net.HttpHeaders;

/**
 * @author Leandro da Silva Vieira
 */
public final class ConstantsWebClient {

    public static final String HEADER_X_FORWARDED_HOST = HttpHeaders.X_FORWARDED_HOST;
    public static final String HEADER_X_FORWARDED_PROTO = HttpHeaders.X_FORWARDED_PROTO;

    public static final String HEADER_X_IBM_CLIENT_ID_KEY = "X-IBM-Client-Id";
    public static final String HEADER_X_IBM_CLIENT_ID_VALUE = "this.sa.clientId";
}
