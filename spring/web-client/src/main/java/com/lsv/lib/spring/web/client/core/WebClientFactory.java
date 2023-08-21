package com.lsv.lib.spring.web.client.core;

import com.lsv.lib.el.jexl.TemplateJexl;
import com.lsv.lib.spring.core.loader.SpringLoader;
import com.lsv.lib.spring.web.client.filter.OAuth2ClientCredentialsFilter;
import com.lsv.lib.spring.web.client.filter.PropagateHeaderFilter;
import com.lsv.lib.spring.web.client.properties.WebClientModuleProperties;
import com.lsv.lib.spring.web.client.properties.WebClientProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

/**
 * Creates and configures WebClient objects.<p>
 * <p>
 * Helpful links:
 * <ul>
 * <li> https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html
 * <li> https://www.baeldung.com/spring-webclient-oauth2
 * </ul>
 *
 * @author Leandro da Silva Vieira
 */
@RequiredArgsConstructor
public class WebClientFactory {

    public static final String PROP_DEFAULT_CONFIGURATION = "defaultConfig";

    private static WebClientFactory instance;

    private final WebClient.Builder webClientBuilder;
    private final WebClientModuleProperties webClientModuleProperties;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public WebClient createWebClientDefault() {
        return createWebClientByConfigurationId(null);
    }

    public WebClient createWebClientByConfigurationId(String configurationId) {
        return createWebClient(ObjectUtils.defaultIfNull(configurationId, PROP_DEFAULT_CONFIGURATION),
            resolveWebClientProperties(configurationId));
    }

    public WebClientProperties resolveWebClientProperties(String configurationId) {
        var defaultWebClientProperties = webClientModuleProperties.getDefaultConfig();
        var finalWebClientProperties = defaultWebClientProperties;

        if (ObjectUtils.isNotEmpty(configurationId)) {
            var webClientProperties = Optional
                .ofNullable(webClientModuleProperties.getConfigurations().get(configurationId))
                .orElseThrow(() -> new IllegalArgumentException("configurationId \"%s\" informado não foi encontrado.".formatted(configurationId)));


            // Merges default properties with specific ones
            finalWebClientProperties = new WebClientProperties()
                .setUrl(ObjectUtils.defaultIfNull(webClientProperties.getUrl(), defaultWebClientProperties.getUrl()))
                .setSa(ObjectUtils.defaultIfNull(webClientProperties.getSa(), defaultWebClientProperties.getSa()))
                .setWebClientBuilderCustomize(ObjectUtils.defaultIfNull(webClientProperties.getWebClientBuilderCustomize(), defaultWebClientProperties.getWebClientBuilderCustomize()))
                .setHttpServiceProxyFactoryCustomize(ObjectUtils.defaultIfNull(webClientProperties.getHttpServiceProxyFactoryCustomize(), defaultWebClientProperties.getHttpServiceProxyFactoryCustomize()))
            ;

            // The lists are summed
            finalWebClientProperties.getPropagateHeaders().addAll(defaultWebClientProperties.getPropagateHeaders());
            finalWebClientProperties.getPropagateHeaders().addAll(webClientProperties.getPropagateHeaders());

            finalWebClientProperties.getFilters().addAll(defaultWebClientProperties.getFilters());
            finalWebClientProperties.getFilters().addAll(webClientProperties.getFilters());

            finalWebClientProperties.getHeaders().putAll(defaultWebClientProperties.getHeaders());
            finalWebClientProperties.getHeaders().putAll(webClientProperties.getHeaders());
        }

        return finalWebClientProperties;
    }

    public WebClient createWebClient(String id, WebClientProperties webClientProperties) {
        if (webClientProperties.getSa() != null) {
            webClientProperties.getFilters().add(() -> OAuth2ClientCredentialsFilter.create(id, webClientProperties.getSa()));
        } else if (Boolean.TRUE.equals(webClientProperties.getEnabledTokenPropagate())) {
            webClientProperties.getPropagateHeaders().add(AUTHORIZATION);
        }

        this.addFilters(webClientProperties, webClientBuilder)
            .addHeaders(webClientProperties, webClientBuilder)
            .webClientBuilder.baseUrl(webClientProperties.getUrl());

        return customize(webClientProperties, webClientBuilder).build();
    }

    public WebClientFactory addFilters(WebClientProperties webClientProperties, WebClient.Builder webClientBuilder) {
        // Adds a filter to propagate the headers
        if (ObjectUtils.isNotEmpty(webClientProperties.getPropagateHeaders())) {
            webClientProperties.getFilters().add(() -> PropagateHeaderFilter.create(webClientProperties.getPropagateHeaders()));
        }
        // Adds all filters to WebClient.Builder
        webClientProperties.getFilters().forEach(filter -> webClientBuilder.filter(filter.get()));

        return this;
    }

    public WebClientFactory addHeaders(WebClientProperties webClientProperties, WebClient.Builder webClientBuilder) {
        var template = new TemplateJexl()
            .adicionarDadoAoContexto("this", webClientProperties)
            .adicionarDadoAoContexto("parent", webClientModuleProperties);

        webClientBuilder.defaultHeaders(httpHeaders ->
            webClientProperties.getHeaders().forEach((key, value) ->
                httpHeaders.set(
                    key,
                    ObjectUtils.defaultIfNull(template.aplicarDadosTemplate(value), value).toString())));

        return this;
    }

    public WebClient.Builder customize(WebClientProperties webClientProperties, WebClient.Builder webClientBuilder) {
        return webClientProperties.getWebClientBuilderCustomize() != null
            ? webClientProperties.getWebClientBuilderCustomize().apply(webClientBuilder)
            : webClientBuilder;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static WebClientFactory instance() {
        if (instance == null) {
            instance = SpringLoader.bean(WebClientFactory.class);
        }
        return instance;
    }
}