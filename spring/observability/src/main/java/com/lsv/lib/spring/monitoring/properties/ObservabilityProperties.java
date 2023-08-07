package com.lsv.lib.spring.monitoring.properties;

import com.lsv.lib.core.audit.AuditProperties;
import com.lsv.lib.core.helper.LibConstants;
import com.lsv.lib.core.properties.LibProperties;
import io.opentelemetry.sdk.metrics.data.AggregationTemporality;
import jakarta.validation.constraints.AssertFalse;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Settings for observability module.
 *
 * @author Leandro da Silva Vieira
 */
@Data

@Validated
@ConfigurationProperties(ObservabilityProperties.PATH)
public class ObservabilityProperties implements LibProperties {

    public static final String PATH = LibConstants.BASE_LIB_PROPERTIES + "observability";

    public static final String PROP_ENABLED = "enabled";
    public static final String PROP_ENABLED_OBSERVED_ASPECT = "enabledObservedAspect";
    public static final String PROP_EXPORT = ObservabilityProperties.PATH + ".export";
    public static final String PROP_ENABLED_METRICS = "enabledMetrics";
    public static final String PROP_ENABLED_TRACES = "enabledTracing";
    public static final String PROP_ENABLED_LOGS = "enabledLogs";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Enables all observability settings.
     */
    private boolean enabled;
    /**
     * Enables the use of @Observed.
     */
    private boolean enabledObservedAspect;
    /**
     * Data export settings.
     */
    private ExportProperties export;
    /**
     * Properties related to the audit.
     */
    private AuditProperties audit;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Data
    public static class ExportProperties {
        /**
         * Enable all data export.
         */
        private boolean enabled;
        /**
         * Endpoint for sending data with the grpc (Google Remote Procedure Call).
         */
        private String endpoint;
        /**
         * Enable tracing data export.
         * The default value follows the export.enabled value.
         */
        private boolean enabledTracing;
        /**
         * Enable metrics data export.
         * The default value follows the export.enabled value.
         */
        private boolean enabledMetrics;
        /**
         * Enable logs data export.
         * The default value follows the export.enabled value.
         */
        private boolean enabledLogs;

// - - - - - - - - - - - - - - - - - - -
// copy of:
// org.springframework.boot.actuate.autoconfigure.tracing.otlp.OtlpProperties
// org.springframework.boot.actuate.autoconfigure.metrics.export.otlp.OtlpProperties

        /**
         * Interval between two consecutive exports.
         */
        private Duration interval = Duration.ofMinutes(1);
        /**
         * Call timeout for the OTel Collector to process an exported batch of data. This
         * timeout spans the entire call: resolving DNS, connecting, writing the request body,
         * server processing, and reading the response body. If the call requires redirects or
         * retries all must complete within one timeout period.
         */
        private Duration timeout = Duration.ofSeconds(10);
        /**
         * Method used to compress the payload.
         */
        private Compression compression = Compression.NONE;
        /**
         * Custom HTTP headers you want to pass to the collector, for example auth headers.
         */
        private Map<String, String> headers = new HashMap<>();
        /**
         * Describes the time period over which measurements are aggregated.
         */
        private AggregationTemporality aggregationTemporality = AggregationTemporality.CUMULATIVE;
        /**
         * Attribute settings of the log that will be exported.
         * @see io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender
         */
        private LogAppender logAppender = new LogAppender();
// - - - - - - - - - - - - - - - - - - -
    }

    @Data
    public static class LogAppender {
        /**
         * @see io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender#setCaptureExperimentalAttributes(boolean) 
         */
        private boolean captureExperimentalAttributes;
        /**
         * @see io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender#setCaptureCodeAttributes(boolean)
         */
        private boolean captureCodeAttributes;
        /**
         * @see io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender#setCaptureMarkerAttribute(boolean) 
         */        
        private boolean captureKeyValuePairAttributes;
        /**
         * @see io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender#setCaptureMarkerAttribute(boolean)
         */
        private boolean captureMarkerAttribute;
    }

    enum Compression {
        /**
         * Gzip compression.
         */
        GZIP,
        /**
         * No compression.
         */
        NONE
    }
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// Validations

    @AssertFalse(message = "endpoint é obrigatório se export.enabled=true")
    public boolean isEndpointRequiredIfEnabledExport() {
        return getExport() != null && getExport().isEnabled() && ObjectUtils.isEmpty(getExport().getEndpoint());
    }
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
}