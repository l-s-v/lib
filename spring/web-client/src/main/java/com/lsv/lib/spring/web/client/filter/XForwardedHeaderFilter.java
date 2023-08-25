package com.lsv.lib.spring.web.client.filter;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import static com.lsv.lib.spring.web.client.helper.ConstantsWebClient.HEADER_X_FORWARDED_HOST;
import static com.lsv.lib.spring.web.client.helper.ConstantsWebClient.HEADER_X_FORWARDED_PROTO;

/**
 * To add X-Forwarded-Host and X-Forwarded-Proto Headers.
 *
 * @author Leandro da Silva Vieira
 */
public class XForwardedHeaderFilter {

    public static ExchangeFilterFunction create() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> Mono.just(
            propagateHeaders(clientRequest)
        ));
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static ClientRequest propagateHeaders(ClientRequest clientRequest) {
        return
            ClientRequest.from(clientRequest)
                .headers(headers -> {
                        headers.set(HEADER_X_FORWARDED_HOST, clientRequest.url().getHost());
                        headers.set(HEADER_X_FORWARDED_PROTO, clientRequest.url().getScheme());
                    }
                )
                .build();
    }
}