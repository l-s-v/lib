package com.lsv.lib.spring.monitoring.config;

import com.lsv.lib.core.audit.Auditable;
import com.lsv.lib.security.web.AllowHttpAccess;
import com.lsv.lib.security.web.properties.HttpMatcher;
import com.lsv.lib.spring.core.annotation.ConditionalAuditEnable;
import com.lsv.lib.spring.core.annotation.YamlSource;
import com.lsv.lib.spring.core.config.SpringCoreAutoConfig;
import com.lsv.lib.spring.core.loader.SpringLoader;
import com.lsv.lib.spring.monitoring.properties.ObservabilityProperties;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

import static com.lsv.lib.spring.core.helper.ConstantsSpring.SUPPRESS_WARNINGS_INJECTION;

/**
 * Autoconfiguration for the observability module.
 * <p>
 * Helpful links:
 * <p>
 * https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html
 * https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.micrometer-tracing
 * https://docs.spring.io/spring-boot/docs/current/actuator-api/htmlsingle
 * <p>
 * https://spring.io/blog/2022/10/12/observability-with-spring-boot-3
 * <p>
 * https://www.baeldung.com/distributed-systems-observability
 * https://www.baeldung.com/spring-boot-3-observability
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j

@YamlSource("classpath:/observability.yaml")
@ObservabilityAutoConfig.ConditionalOnEnabledObservability
@AutoConfiguration(after = {SpringCoreAutoConfig.class})
@EnableConfigurationProperties(ObservabilityProperties.class)
public class ObservabilityAutoConfig {

    /**
     * Frees access to actuator pages, adding one processing to run on the security part.
     */
    @Bean
    public AllowHttpAccess actuatorAllowHttpAccess(@Value("${management.endpoints.web.base-path:/actuator}") String basePath) {
        return () -> HttpMatcher.withPatternsAndMethod("GET", basePath + "/**");
    }

    /**
     * Enabling the use of @Observed.
     * <p>
     * https://www.baeldung.com/spring-boot-3-observability#3-aop
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnEnabledObservedAspect
    public ObservedAspect observedAspect(@SuppressWarnings(SUPPRESS_WARNINGS_INJECTION) ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }

    /**
     * Assembles the observability module audit data.
     */
    @Bean
    @ConditionalAuditEnable(ObservabilityProperties.PATH)
    public Auditable observabilityAuditable() {
        String ID = "observability";

        return new Auditable() {
            @Override
            public String id() {
                return ID;
            }

            @Override
            public Map<String, String> geData() {
                try {
                    var trace = SpringLoader.bean(Tracer.class);

                    return Map.of(
                        "traceid", trace.currentSpan().context().traceId()
                    );
                } catch (Throwable e) {
                    log.warn("Não foi possível recuperar os dados de auditoria de observability", e);
                    return null;
                }
            }
        };
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// Local conditionals to facilitate hierarchical validation

    // @formatter:off
    @Retention(RetentionPolicy.RUNTIME)
    @ConditionalOnProperty(
        havingValue = "true",
        matchIfMissing = true
    )
    @interface ConditionalLocalDefault {

        @AliasFor(annotation = ConditionalOnProperty.class, attribute = "prefix")
        String prefix() default ObservabilityProperties.PATH;

        @AliasFor(annotation = ConditionalOnProperty.class, attribute = "name")
        String name() default ObservabilityProperties.PROP_ENABLED;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @ConditionalLocalDefault
    public @interface ConditionalOnEnabledObservability {}

    @Retention(RetentionPolicy.RUNTIME)
    @ConditionalOnEnabledObservability
    @ConditionalLocalDefault(name = ObservabilityProperties.PROP_ENABLED_OBSERVED_ASPECT)
    @interface ConditionalOnEnabledObservedAspect {}
    // @formatter:on
}