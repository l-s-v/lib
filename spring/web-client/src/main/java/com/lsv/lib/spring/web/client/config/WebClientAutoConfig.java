package com.lsv.lib.spring.web.client.config;

import com.lsv.lib.spring.core.annotation.YamlSource;
import com.lsv.lib.spring.core.config.SpringCoreAutoConfig;
import com.lsv.lib.spring.web.client.core.HttpExchangeClientRegister;
import com.lsv.lib.spring.web.client.core.WebClientFactory;
import com.lsv.lib.spring.web.client.properties.WebClientModuleProperties;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;

/**
 * Autoconfiguration for the web client module.
 *
 * @author Leandro da Silva Vieira
 */
@YamlSource("classpath:/web-client.yaml")
@AutoConfiguration(after = SpringCoreAutoConfig.class)
@EnableConfigurationProperties(WebClientModuleProperties.class)
public class WebClientAutoConfig {

    private final ReactorClientHttpConnector clientHttpConnector = new ReactorClientHttpConnector(
        HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE));

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Bean
    @ConditionalOnMissingBean
    public WebClientFactory defaultWebClientFactory(WebClientModuleProperties webClientModuleProperties) {
        return new WebClientFactory(webClientModuleProperties);
    }

    /**
     * Registers all beans that use @HttpExchangeClient.
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpExchangeClientRegister defaultHttpExchangeClientRegister(WebClientModuleProperties webClientModuleProperties,
                                                                        WebClientFactory webClientFactory) {
        return new HttpExchangeClientRegister(webClientModuleProperties, webClientFactory);
    }

    /**
     * Forces the creation of HttpExchangeClientRegister before starting to create other objects.
     */
    @Bean
    public BeanPostProcessor forceLoadHttpExchangeClientRegister(HttpExchangeClientRegister httpExchangeClientRegister) {
        return new BeanPostProcessor() {};
    }

    /**
     * Resolves problem https://github.com/reactor/reactor-netty/issues/1431.
     */
    @Bean
    public WebClientCustomizer fixHttpClientResolve() {
        return webClientBuilder -> webClientBuilder.clientConnector(clientHttpConnector);
    }
}
