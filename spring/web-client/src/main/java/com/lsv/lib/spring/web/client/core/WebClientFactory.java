package com.lsv.lib.spring.web.client.core;

import com.lsv.lib.el.jexl.TemplateJexl;
import com.lsv.lib.spring.core.loader.SpringLoader;
import com.lsv.lib.spring.web.client.filter.OAuth2ClientCredentialsFilter;
import com.lsv.lib.spring.web.client.filter.PropagateHeaderFilter;
import com.lsv.lib.spring.web.client.filter.XForwardedHeaderFilter;
import com.lsv.lib.spring.web.client.properties.WebClientModuleProperties;
import com.lsv.lib.spring.web.client.properties.WebClientProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static com.lsv.lib.spring.web.client.helper.ConstantsWebClient.HEADER_X_IBM_CLIENT_ID_KEY;
import static com.lsv.lib.spring.web.client.helper.ConstantsWebClient.HEADER_X_IBM_CLIENT_ID_VALUE;

/**
 * Creates and configures WebClient objects.
 *
 * @author Leandro da Silva Vieira
 * @see <a href="https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html">webflux-webclient</a>
 * @see <a href="https://www.baeldung.com/spring-webclient-oauth2">spring-webclient-oauth2</a>
 */
@RequiredArgsConstructor
public class WebClientFactory {

    public static final String PROP_DEFAULT_CONFIGURATION = "defaultConfig";

    private static WebClientFactory instance;

    private final WebClientModuleProperties webClientModuleProperties;
    private WebClient.Builder webClientBuilder;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public WebClient createWebClientDefault() {
        return createWebClientByConfigurationId(null);
    }

    public WebClient createWebClientByConfigurationId(String configurationId) {
        return createWebClient(
            ObjectUtils.defaultIfNull(configurationId, PROP_DEFAULT_CONFIGURATION),
            resolveWebClientProperties(configurationId));
    }

    public WebClientProperties resolveWebClientProperties(String configurationId) {
        var finalWebClientProperties = webClientModuleProperties.getDefaultConfig();

        if (ObjectUtils.isNotEmpty(configurationId)) {
            var webClientProperties = Optional
                .ofNullable(webClientModuleProperties.getConfigurations().get(configurationId))
                .orElseThrow(() -> new IllegalArgumentException("configurationId \"%s\" informado não foi encontrado.".formatted(configurationId)));


            var defaultWebClientProperties = finalWebClientProperties;

            // Merges default properties with specific ones
            finalWebClientProperties = new WebClientProperties()
                .setUrl(ObjectUtils.defaultIfNull(webClientProperties.getUrl(), defaultWebClientProperties.getUrl()))
                .setSa(ObjectUtils.defaultIfNull(webClientProperties.getSa(), defaultWebClientProperties.getSa()))
                .setWebClientBuilderCustomize(ObjectUtils.defaultIfNull(webClientProperties.getWebClientBuilderCustomize(), defaultWebClientProperties.getWebClientBuilderCustomize()))
                .setHttpServiceProxyFactoryCustomize(ObjectUtils.defaultIfNull(webClientProperties.getHttpServiceProxyFactoryCustomize(), defaultWebClientProperties.getHttpServiceProxyFactoryCustomize()))
                .setShortcut(new WebClientProperties.Shortcut()
                    .setPropagateToken(ObjectUtils.defaultIfNull(webClientProperties.getShortcut().getPropagateToken(), defaultWebClientProperties.getShortcut().getPropagateToken()))
                    .setHeaderApic(ObjectUtils.defaultIfNull(webClientProperties.getShortcut().getHeaderApic(), defaultWebClientProperties.getShortcut().getHeaderApic()))
                    .setHeaderApicPropagate(ObjectUtils.defaultIfNull(webClientProperties.getShortcut().getHeaderApicPropagate(), defaultWebClientProperties.getShortcut().getHeaderApicPropagate()))
                    .setHeaderXForwarded(ObjectUtils.defaultIfNull(webClientProperties.getShortcut().getHeaderXForwarded(), defaultWebClientProperties.getShortcut().getHeaderXForwarded()))
                )
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
        // @formatter:off
        return
            customize(webClientProperties,
                    handleShortcuts(webClientProperties)
                   .authenticationConfigure(id, webClientProperties)
                   .addFilters(webClientProperties, getWebClientBuilder())
                   .addHeaders(webClientProperties, getWebClientBuilder())
                   .getWebClientBuilder().baseUrl(webClientProperties.getUrl())
            ).build();
        // @formatter:on
    }

    public WebClientFactory handleShortcuts(WebClientProperties webClientProperties) {
        if (Boolean.TRUE.equals(webClientProperties.getShortcut().getPropagateToken())) {
            webClientProperties.getPropagateHeaders().add(AUTHORIZATION);
        }

        if (Boolean.TRUE.equals(webClientProperties.getShortcut().getHeaderApic())) {
            webClientProperties.getHeaders().put(HEADER_X_IBM_CLIENT_ID_KEY, HEADER_X_IBM_CLIENT_ID_VALUE);
        }
        if (Boolean.TRUE.equals(webClientProperties.getShortcut().getHeaderApicPropagate())) {
            webClientProperties.getPropagateHeaders().add(HEADER_X_IBM_CLIENT_ID_KEY);
        }

        if (Boolean.TRUE.equals(webClientProperties.getShortcut().getHeaderXForwarded())) {
            webClientProperties.getFilters().add(XForwardedHeaderFilter::create);
        }

        return this;
    }

    public WebClientFactory authenticationConfigure(String id, WebClientProperties webClientProperties) {
        if (webClientProperties.getSa() != null) {
            webClientProperties.getFilters().add(() -> OAuth2ClientCredentialsFilter.create(id, webClientProperties.getSa()));
            webClientProperties.getPropagateHeaders().remove(AUTHORIZATION);
        }

        return this;
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

    /**
     * Delay loading as much as possible, as injecting it during the creation of Beans caused tracing to not work correctly.
     */
    public WebClient.Builder getWebClientBuilder() {
        if (webClientBuilder == null) {
            webClientBuilder = SpringLoader.bean(WebClient.Builder.class);
        }
        return webClientBuilder;
    }
}