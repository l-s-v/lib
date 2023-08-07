package com.lsv.lib.spring.monitoring.audit;

import com.lsv.lib.core.audit.Auditable;
import com.lsv.lib.spring.core.annotation.ConditionalAuditEnable;
import com.lsv.lib.spring.core.loader.SpringLoader;
import com.lsv.lib.spring.monitoring.config.ObservabilityAutoConfig.ConditionalOnEnabledObservability;
import com.lsv.lib.spring.monitoring.properties.ObservabilityProperties;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Monta os dados de auditoria do módulo de observabilidade.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j

@ConditionalOnEnabledObservability
@ConditionalAuditEnable(ObservabilityProperties.PATH)
@Component
public class ObservabilityAuditable implements Auditable {

    private static final String ID = "observability";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

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
}
