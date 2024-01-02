package com.lsv.lib.spring.monitoring.config;

import ch.qos.logback.classic.LoggerContext;
import com.lsv.lib.spring.monitoring.config.ObservabilityAutoConfig.ConditionalLocalDefault;
import com.lsv.lib.spring.monitoring.config.ObservabilityAutoConfig.ConditionalOnEnabledObservability;
import com.lsv.lib.spring.monitoring.properties.ObservabilityProperties;
import com.lsv.lib.spring.monitoring.properties.ObservabilityProperties.ExportProperties;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
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
import io.opentelemetry.semconv.ResourceAttributes;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.lsv.lib.spring.core.helper.ConstantsSpring.SPRING_APPLICATION_NAME;
import static com.lsv.lib.spring.core.helper.ConstantsSpring.SUPPRESS_WARNINGS_INJECTION;

/**
 * Automatic configuration for export using OpenTelemetry (Otel).
 *
 * @see <a href="https://opentelemetry.io/docs/instrumentation/java/manual/">opentelemetry</a>
 * @see <a href="https://opentelemetry.io/docs/collector/deployment/gateway/">opentelemetry gateway</a>
 *
 * @author Leandro da Silva Vieira
 */
@ObservabilityOtelAutoConfig.ConditionalOnEnabledObservabilityExport
@AutoConfiguration(after = {ObservabilityAutoConfig.class})
public class ObservabilityOtelAutoConfig {

    static Resource RESOURCE;

    public ObservabilityOtelAutoConfig(@Value(SPRING_APPLICATION_NAME) String applicationName) {
        RESOURCE = Resource.create(
            Attributes.of(ResourceAttributes.SERVICE_NAME,
                ObjectUtils.defaultIfNull(applicationName, "application")));
    }

    @Bean
    public ExportProperties exportProperties(@SuppressWarnings(SUPPRESS_WARNINGS_INJECTION) ObservabilityProperties observabilityProperties) {
        return observabilityProperties.getExport();
    }
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

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
        public OtlpGrpcMetricExporter defaultOtlpGrpcMetricExporter(@SuppressWarnings(SUPPRESS_WARNINGS_INJECTION) ExportProperties exportProperties) {
            var builder = OtlpGrpcMetricExporter.builder()
                .setEndpoint(exportProperties.getEndpoint())
                .setTimeout(exportProperties.getTimeout())
                .setCompression(String.valueOf(exportProperties.getCompression()).toLowerCase())
                .setAggregationTemporalitySelector(AggregationTemporality.DELTA.equals(exportProperties.getAggregationTemporality())
                    ? AggregationTemporalitySelector.deltaPreferred()
                    : AggregationTemporalitySelector.alwaysCumulative());

            exportProperties.getHeaders().forEach(builder::addHeader);

            return builder.build();
        }

        /**
         * Configure metrics provider.
         */
        @Bean
        @ConditionalOnMissingBean
        public SdkMeterProvider defaultSdkMeterProvider(MetricExporter metricExporter,
                                                        @SuppressWarnings(SUPPRESS_WARNINGS_INJECTION) ExportProperties exportProperties) {
            return SdkMeterProvider.builder()
                .registerMetricReader(PeriodicMetricReader
                    .builder(metricExporter)
                    .setInterval(exportProperties.getInterval())
                    .build())
                .setResource(RESOURCE)
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
    public OtlpGrpcSpanExporter defaultOtlpGrpcSpanExporter(ExportProperties exportProperties,
                                                            @Autowired(required = false) SdkMeterProvider sdkMeterProvider) {
        var builder = OtlpGrpcSpanExporter.builder()
            .setEndpoint(exportProperties.getEndpoint())
            .setTimeout(exportProperties.getTimeout())
            .setCompression(String.valueOf(exportProperties.getCompression()).toLowerCase());

        if (sdkMeterProvider != null) {
            builder.setMeterProvider(sdkMeterProvider);
        }

        exportProperties.getHeaders().forEach(builder::addHeader);

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
        public OtlpGrpcLogRecordExporter defaultOtlpGrpcLogRecordExporter(@SuppressWarnings(SUPPRESS_WARNINGS_INJECTION) ExportProperties exportProperties,
                                                                          @Autowired(required = false) SdkMeterProvider sdkMeterProvider) {
            var builder = OtlpGrpcLogRecordExporter.builder()
                .setEndpoint(exportProperties.getEndpoint())
                .setTimeout(exportProperties.getTimeout())
                .setCompression(String.valueOf(exportProperties.getCompression()).toLowerCase());

            if (sdkMeterProvider != null) {
                builder.setMeterProvider(sdkMeterProvider);
            }

            exportProperties.getHeaders().forEach(builder::addHeader);

            return builder.build();
        }

        /**
         * Configure logs provider.
         */
        @Bean
        @ConditionalOnMissingBean
        public SdkLoggerProvider defaultSdkLoggerProvider(LogRecordExporter logExporter,
                                                          @SuppressWarnings(SUPPRESS_WARNINGS_INJECTION) ExportProperties exportProperties,
                                                          @Autowired(required = false) SdkMeterProvider sdkMeterProvider) {

            var recordProcessorBuilder = BatchLogRecordProcessor
                .builder(logExporter)
                .setScheduleDelay(exportProperties.getInterval())
                .setExporterTimeout(exportProperties.getTimeout());

            if (sdkMeterProvider != null) {
                recordProcessorBuilder.setMeterProvider(sdkMeterProvider);
            }

            return SdkLoggerProvider.builder()
                .addLogRecordProcessor(recordProcessorBuilder.build())
                .setResource(RESOURCE)
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
     *
     * @see org.springframework.boot.actuate.autoconfigure.tracing.OpenTelemetryAutoConfiguration
     */
    @Bean
    @ConditionalOnMissingBean
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

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// Local conditionals to facilitate hierarchical validation

    // @formatter:off
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
    // @formatter:on
}