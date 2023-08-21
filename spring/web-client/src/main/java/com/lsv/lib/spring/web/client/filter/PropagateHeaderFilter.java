package com.lsv.lib.spring.web.client.filter;

import com.lsv.lib.spring.web.commons.helper.WebSpringHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.List;
import java.util.Optional;

/**
 * Creates the filter that allows propagating headers from the source request to the next request.
 *
 * @author Leandro da Silva Vieira
 */
public class PropagateHeaderFilter {

    public static ExchangeFilterFunction create(List<String> headersToPropagate) {
        if (ObjectUtils.isEmpty(headersToPropagate)) {
            return null;
        }

        return (clientRequest, next) -> next.exchange(
            propagateHeaders(clientRequest, headersToPropagate, WebSpringHelper.getCurrentHttpRequest())
        );
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static ClientRequest propagateHeaders(ClientRequest nextClientRequest, List<String> headersToPropagate, HttpServletRequest sourceHttpServletRequest) {
        return
            sourceHttpServletRequest == null ? nextClientRequest :
                ClientRequest.from(nextClientRequest)
                    .headers(nextHeaders ->
                        headersToPropagate.forEach(headerKey ->
                            Optional
                                .ofNullable(sourceHttpServletRequest.getHeader(headerKey))
                                .ifPresent(headerValue -> nextHeaders.set(headerKey, headerValue))
                        )
                    )
                    .build();
    }
}