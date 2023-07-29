package com.lsv.lib.spring.monitoring.config;

import ch.qos.logback.classic.LoggerContext;
import com.lsv.lib.security.web.AllowHttpAccess;
import com.lsv.lib.security.web.properties.HttpMatcher;
import com.lsv.lib.spring.core.annotation.YamlSource;
import com.lsv.lib.spring.core.config.SpringCoreAutoConfig;
import com.lsv.lib.spring.monitoring.properties.ObservabilityProperties;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import io.opentelemetry.instrumentation.micrometer.v1_5.OpenTelemetryMeterRegistry;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.logs.export.LogRecordExporter;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.data.AggregationTemporality;
import io.opentelemetry.sdk.metrics.export.AggregationTemporalitySelector;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.env.Environment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.lsv.lib.spring.core.helper.ConstantsSpring.SPRING_APPLICATION_NAME;
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
 * <p>
 * https://opentelemetry.io/docs/collector/deployment/gateway/
 *
 * @author Leandro da Silva Vieira
 */
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

// - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Concentrates export settings.
     * <p>
     * https://opentelemetry.io/docs/instrumentation/java/manual/
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnEnabledObservabilityExport
    static class OtlpExporter {

        static Resource resource;

        public OtlpExporter(Environment environment) {
            resource = Resource.create(
                Attributes.of(ResourceAttributes.SERVICE_NAME,
                    environment.getProperty(SPRING_APPLICATION_NAME, "application")));
        }

// - - - - - - - - - - - - - - - - - - - - - - - - -

        /**
         * Concentrates metrics export settings.
         */
        @Configuration(proxyBeanMethods = false)
        @ConditionalOnEnabledObservabilityExportMetrics
        static class OtlpMetricExporter {

            /**
             * Force use protocol GRPC instead HTTP for metrics.
             */
            @Bean
            @ConditionalOnMissingBean
            public OtlpGrpcMetricExporter defaultOtlpGrpcMetricExporter(@SuppressWarnings(SUPPRESS_WARNINGS_INJECTION) ObservabilityProperties observabilityProperties) {
                var builder = OtlpGrpcMetricExporter.builder()
                    .setEndpoint(observabilityProperties.getExport().getEndpoint())
                    .setTimeout(observabilityProperties.getExport().getTimeout())
                    .setCompression(String.valueOf(observabilityProperties.getExport().getCompression()).toLowerCase())
                    .setAggregationTemporalitySelector(AggregationTemporality.DELTA.equals(observabilityProperties.getExport().getAggregationTemporality())
                        ? AggregationTemporalitySelector.deltaPreferred()
                        : AggregationTemporalitySelector.alwaysCumulative());

                observabilityProperties.getExport().getHeaders().forEach(builder::addHeader);

                return builder.build();
            }

            /**
             * Configure metrics provider.
             */
            @Bean
            @ConditionalOnMissingBean
            public SdkMeterProvider defaultSdkMeterProvider(MetricExporter metricExporter,
                                                            @SuppressWarnings(SUPPRESS_WARNINGS_INJECTION) ObservabilityProperties observabilityProperties) {
                return SdkMeterProvider.builder()
                    .registerMetricReader(PeriodicMetricReader
                        .builder(metricExporter)
                        .setInterval(observabilityProperties.getExport().getInterval())
                        .build())
                    .setResource(OtlpExporter.resource)
                    .build();
            }

            /**
             * Integrates Micrometer metrics with OpenTelemetry.
             */
            @Bean
            @ConditionalOnMissingBean
            @SuppressWarnings(SUPPRESS_WARNINGS_INJECTION)
            public MeterRegistry defaultOpenTelemetryMeterRegistry(OpenTelemetry openTelemetry, Clock clock) {
                return OpenTelemetryMeterRegistry.builder(openTelemetry)
                    .setMicrometerHistogramGaugesEnabled(true)
                    .setClock(clock)
                    .build();
            }
        }

// - - - - - - - - - - - - - - - - - - - - - - - - -

        /**
         * Replacing bean create in OtlpAutoConfiguration.
         * Force use protocol GRPC instead HTTP for traces.
         */
        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnEnabledObservabilityExportTrace
        public OtlpGrpcSpanExporter defaultOtlpGrpcSpanExporter(@SuppressWarnings(SUPPRESS_WARNINGS_INJECTION) ObservabilityProperties observabilityProperties,
                                                                @Autowired(required = false) SdkMeterProvider sdkMeterProvider) {
            var builder = OtlpGrpcSpanExporter.builder()
                .setEndpoint(observabilityProperties.getExport().getEndpoint())
                .setTimeout(observabilityProperties.getExport().getTimeout())
                .setCompression(String.valueOf(observabilityProperties.getExport().getCompression()).toLowerCase());

            if (sdkMeterProvider != null) {
                builder.setMeterProvider(sdkMeterProvider);
            }

            observabilityProperties.getExport().getHeaders().forEach(builder::addHeader);

            return builder.build();
        }

// - - - - - - - - - - - - - - - - - - - - - - - - -

        /**
         * Concentrates logs export settings.
         * <p>
         * LogbackConfigurator
         */
        @Configuration(proxyBeanMethods = false)
        @ConditionalOnEnabledObservabilityExportLogs
        static class OtlpLogExporter {

            /**
             * Force use protocol GRPC instead HTTP for logs.
             */
            @Bean
            @ConditionalOnMissingBean
            public OtlpGrpcLogRecordExporter defaultOtlpGrpcLogRecordExporter(@SuppressWarnings(SUPPRESS_WARNINGS_INJECTION) ObservabilityProperties observabilityProperties,
                                                                              @Autowired(required = false) SdkMeterProvider sdkMeterProvider) {
                var builder = OtlpGrpcLogRecordExporter.builder()
                    .setEndpoint(observabilityProperties.getExport().getEndpoint())
                    .setTimeout(observabilityProperties.getExport().getTimeout())
                    .setCompression(String.valueOf(observabilityProperties.getExport().getCompression()).toLowerCase());

                if (sdkMeterProvider != null) {
                    builder.setMeterProvider(sdkMeterProvider);
                }

                observabilityProperties.getExport().getHeaders().forEach(builder::addHeader);

                return builder.build();
            }

            /**
             * Configure logs provider.
             */
            @Bean
            @ConditionalOnMissingBean
            public SdkLoggerProvider defaultSdkLoggerProvider(LogRecordExporter logExporter,
                                                              @SuppressWarnings(SUPPRESS_WARNINGS_INJECTION) ObservabilityProperties observabilityProperties,
                                                              @Autowired(required = false) SdkMeterProvider sdkMeterProvider) {

                var recordProcessorBuilder = BatchLogRecordProcessor
                    .builder(logExporter)
                    .setScheduleDelay(observabilityProperties.getExport().getInterval())
                    .setExporterTimeout(observabilityProperties.getExport().getTimeout());

                if (sdkMeterProvider != null) {
                    recordProcessorBuilder.setMeterProvider(sdkMeterProvider);
                }

                return SdkLoggerProvider.builder()
                    .addLogRecordProcessor(recordProcessorBuilder.build())
                    .setResource(OtlpExporter.resource)
                    .build();
            }

            /**
             * Configure OpenTelemetryAppender programmatically to not interfere with the original
             * logs and allow manipulating the settings.
             */
            @Bean
            @ConditionalOnMissingBean
            @SuppressWarnings(SUPPRESS_WARNINGS_INJECTION)
            public OpenTelemetryAppender defaultOpenTelemetryAppender(OpenTelemetry openTelemetry,
                                                                      ObservabilityProperties observabilityProperties) {
                var openTelemetryAppender = new OpenTelemetryAppender();
                openTelemetryAppender.setOpenTelemetry(openTelemetry);
                openTelemetryAppender.setName("OpenTelemetry");

                var logAppender = observabilityProperties.getExport().getLogAppender();
                openTelemetryAppender.setCaptureExperimentalAttributes(logAppender.isCaptureExperimentalAttributes());
                openTelemetryAppender.setCaptureCodeAttributes(logAppender.isCaptureCodeAttributes());
                openTelemetryAppender.setCaptureKeyValuePairAttributes(logAppender.isCaptureKeyValuePairAttributes());
                openTelemetryAppender.setCaptureMarkerAttribute(logAppender.isCaptureMarkerAttribute());

                return openTelemetryAppender;
            }

            /**
             * Avoid spring cyclic dependency.
             */
            @Configuration(proxyBeanMethods = false)
            static class LogbackConfigLog {

                /**
                 * Add OpenTelemetryAppender to the logback context.
                 */
                @Autowired(required = false)
                @SuppressWarnings(SUPPRESS_WARNINGS_INJECTION)
                public void addAppenderOpenTelemetry(OpenTelemetryAppender openTelemetryAppender) {
                    if (LoggerFactory.getILoggerFactory() instanceof LoggerContext logCtx) {
                        openTelemetryAppender.setContext(logCtx);
                        openTelemetryAppender.start();

                        logCtx.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(openTelemetryAppender);
                    }
                }
            }
        }

// - - - - - - - - - - - - - - - - - - - - - - - - -

        /**
         * Replacing bean create in OpenTelemetryAutoConfiguration.
         * Uses all features (metrics, logs, traces) with OpenTelimetry.
         */
        @Bean
        @ConditionalOnMissingBean
        @SuppressWarnings(SUPPRESS_WARNINGS_INJECTION)
        public OpenTelemetry defaultOpenTelemetry(@Autowired(required = false) SdkMeterProvider sdkMeterProvider,
                                                  @Autowired(required = false) SdkTracerProvider sdkTracerProvider,
                                                  @Autowired(required = false) SdkLoggerProvider sdkLoggerProvider,
                                                  @Autowired(required = false) ContextPropagators contextPropagators) {

            if (ObjectUtils.allNull(sdkMeterProvider, sdkTracerProvider, sdkLoggerProvider)) {
                return OpenTelemetry.noop();
            }

            return OpenTelemetrySdk.builder()
                .setMeterProvider(sdkMeterProvider)
                .setTracerProvider(sdkTracerProvider)
                .setLoggerProvider(sdkLoggerProvider)
                .setPropagators(contextPropagators)
                .buildAndRegisterGlobal();
        }
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
    @interface ConditionalOnEnabledObservability {}

    @Retention(RetentionPolicy.RUNTIME)
    @ConditionalOnEnabledObservability
    @ConditionalLocalDefault(prefix = ObservabilityProperties.PROP_EXPORT)
    @interface ConditionalOnEnabledObservabilityExport {}

    @Retention(RetentionPolicy.RUNTIME)
    @ConditionalOnEnabledObservabilityExport
    @ConditionalLocalDefault(prefix = ObservabilityProperties.PROP_EXPORT, name = ObservabilityProperties.PROP_ENABLED_METRICS)
    @interface ConditionalOnEnabledObservabilityExportMetrics {}

    @Retention(RetentionPolicy.RUNTIME)
    @ConditionalOnEnabledObservabilityExport
    @ConditionalLocalDefault(prefix = ObservabilityProperties.PROP_EXPORT, name = ObservabilityProperties.PROP_ENABLED_TRACES)
    @interface ConditionalOnEnabledObservabilityExportTrace {}

    @Retention(RetentionPolicy.RUNTIME)
    @ConditionalOnEnabledObservabilityExport
    @ConditionalLocalDefault(prefix = ObservabilityProperties.PROP_EXPORT, name = ObservabilityProperties.PROP_ENABLED_LOGS)
    @interface ConditionalOnEnabledObservabilityExportLogs {}

    @Retention(RetentionPolicy.RUNTIME)
    @ConditionalOnEnabledObservability
    @ConditionalLocalDefault(name = ObservabilityProperties.PROP_ENABLED_OBSERVED_ASPECT)
    @interface ConditionalOnEnabledObservedAspect {}
    // @formatter:on
}