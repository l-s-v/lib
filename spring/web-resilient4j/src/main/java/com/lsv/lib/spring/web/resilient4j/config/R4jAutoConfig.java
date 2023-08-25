package com.lsv.lib.spring.web.resilient4j.config;

import com.lsv.lib.core.function.Resolver;
import com.lsv.lib.spring.core.annotation.YamlSource;
import com.lsv.lib.spring.core.config.SpringCoreAutoConfig;
import com.lsv.lib.spring.web.commons.event.WebClientRequestInterceptEvent;
import com.lsv.lib.spring.web.resilient4j.core.R4jOperator;
import com.lsv.lib.spring.web.resilient4j.core.R4jOperatorResolverByEvent;
import com.lsv.lib.spring.web.resilient4j.core.R4jOperatorResolverByInstance;
import com.lsv.lib.spring.web.resilient4j.core.R4jWebClientRequestInterceptEventListener;
import com.lsv.lib.spring.web.resilient4j.properties.R4jInstance;
import com.lsv.lib.spring.web.resilient4j.properties.R4jModuleProperties;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Collection;

/**
 * Autoconfiguration for the resilient4j module.
 *
 * @see <a href="https://resilience4j.readme.io/docs">docs</a>
 * @see <a href="https://github.com/resilience4j/resilience4j">github</a>
 * @see <a href="https://github.com/resilience4j/resilience4j-spring-boot3-demo">example resilience4j-spring-boot3</a>
 * @see <a href="https://www.baeldung.com/resilience4j">baeldung resilience4j</a>
 * @see <a href="https://arnoldgalovics.com/resilience4j-webclient">resilience4j-webclient</a>
 * @see <a href="LINK">LABEL</a>
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j

@YamlSource("classpath:/resilient4j.yaml")
@ConditionalOnProperty(
    prefix = R4jModuleProperties.PATH,
    name = R4jModuleProperties.PROP_ENABLED,
    havingValue = "true"
)
@AutoConfiguration(after = {SpringCoreAutoConfig.class})
@EnableConfigurationProperties(R4jModuleProperties.class)
public class R4jAutoConfig {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Listener for the audit manual event.
     */
    @Bean
    @ConditionalOnMissingBean
    public R4jWebClientRequestInterceptEventListener defaultR4jWebClientRequestInterceptEventListener(
        Resolver<WebClientRequestInterceptEvent, Collection<R4jOperator<?>>> operatorResolverByEvent
    ) {
        log.trace("Configurado listener de {}", WebClientRequestInterceptEvent.class.getName());
        return new R4jWebClientRequestInterceptEventListener(operatorResolverByEvent);
    }

    /**
     * Crosses the request event information with the resilient4j settings.
     */
    @Bean
    @ConditionalOnMissingBean
    public R4jOperatorResolverByEvent defaultR4jOperatorResolverByEvent(
        R4jModuleProperties r4jModuleProperties,
        Resolver<R4jInstance, R4jOperator<?>> operatorResolverByInstance
    ) {
        return new R4jOperatorResolverByEvent(r4jModuleProperties, operatorResolverByInstance);
    }

    /**
     * Unifies all records with Resiliente4J settings in a single location to facilitate data recovery.
     */
    @Bean
    @ConditionalOnMissingBean
    public R4jOperatorResolverByInstance defaultR4jOperatorResolverByInstance(
        CircuitBreakerRegistry circuitBreakerRegistry,
        BulkheadRegistry bulkheadRegistry,
        RetryRegistry retryRegistry,
        RateLimiterRegistry rateLimiterRegistry,
        TimeLimiterRegistry timeLimiterRegistry
    ) {
        return new R4jOperatorResolverByInstance(
            circuitBreakerRegistry,
            bulkheadRegistry,
            retryRegistry,
            rateLimiterRegistry,
            timeLimiterRegistry
        );
    }
}