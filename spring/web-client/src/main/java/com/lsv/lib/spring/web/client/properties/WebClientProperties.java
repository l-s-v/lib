package com.lsv.lib.spring.web.client.properties;

import com.lsv.lib.security.web.properties.oidc.ServiceAccount;
import com.lsv.lib.spring.web.client.annotation.HttpExchangeClient;
import lombok.Data;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Configurations for a WebClient.
 *
 * @author Leandro da Silva Vieira
 */
@Data
public class WebClientProperties {

    /**
     * Base URL for the request. Is ignored if @HttpExchangeClient.url is used;;
     *
     * @see WebClient.Builder#baseUrl(String)
     * @see HttpExchangeClient#url()
     */
    private String url;
    /**
     * Service account data.
     */
    private ServiceAccount sa;
    /**
     * Propagate the current token, if it exists.
     * Valid only if no service account is provided.
     */
    private Boolean enabledTokenPropagate;
    /**
     * Headers that will be added to the request.
     * Accepts jexl for expressions in values.
     * The root object will be the current WebClientProperties.
     */
    private Map<String, String> headers = new HashMap<>();
    /**
     * Headers that should be copied from the previous request, IF THEY EXIST;
     */
    private List<String> propagateHeaders = new LinkedList<>();
    /**
     * Classes that will provide (Supplier) ExchangeFilterFunction.
     */
    private List<Supplier<ExchangeFilterFunction>> filters = new LinkedList<>();
    /**
     * Allows you to customize (even replace) the WebClient.Builder that will be used to build the WebClient.
     */
    private Function<WebClient.Builder, WebClient.Builder> webClientBuilderCustomize;
    /**
     * Lets you override the creation of the HttpServiceProxyFactory.
     */
    private Function<WebClient, HttpServiceProxyFactory> httpServiceProxyFactoryCustomize;
}