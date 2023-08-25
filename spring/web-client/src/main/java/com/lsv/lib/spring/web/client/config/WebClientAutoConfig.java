package com.lsv.lib.spring.web.client.config;

import com.lsv.lib.spring.core.annotation.YamlSource;
import com.lsv.lib.spring.core.config.SpringCoreAutoConfig;
import com.lsv.lib.spring.web.client.core.HttpExchangeClientRegister;
import com.lsv.lib.spring.web.client.core.WebClientFactory;
import com.lsv.lib.spring.web.client.properties.WebClientModuleProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import static com.lsv.lib.spring.core.helper.ConstantsSpring.SUPPRESS_WARNINGS_INJECTION;

/**
 * Autoconfiguration for the web client module.
 *
 * @author Leandro da Silva Vieira
 */
@YamlSource("classpath:/web-client.yaml")
@AutoConfiguration(after = SpringCoreAutoConfig.class)
@EnableConfigurationProperties(WebClientModuleProperties.class)
public class WebClientAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public WebClientFactory defaultWebClientFactory(@SuppressWarnings(SUPPRESS_WARNINGS_INJECTION) WebClient.Builder webClientBuilder,
                                                    WebClientModuleProperties webClientModuleProperties) {
        return new WebClientFactory(webClientBuilder, webClientModuleProperties);
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
}