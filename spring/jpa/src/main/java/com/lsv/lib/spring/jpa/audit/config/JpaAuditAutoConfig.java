package com.lsv.lib.spring.jpa.audit.config;

import com.lsv.lib.core.audit.AuditProperties;
import com.lsv.lib.spring.core.annotation.ConditionalAuditEnable;
import com.lsv.lib.spring.jpa.audit.event.JpaAuditEvent;
import com.lsv.lib.spring.jpa.audit.repository.AuditRepository;
import com.lsv.lib.spring.jpa.config.JpaAutoConfig;
import com.lsv.lib.spring.jpa.properties.JpaProperties;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import static com.lsv.lib.spring.core.helper.ConstantsSpring.SUPPRESS_WARNINGS_INJECTION;

/**
 * Autoconfiguration for the jpa audit module.
 *
 * @author Leandro da Silva Vieira
 */
@ConditionalAuditEnable(JpaProperties.PATH)
@AutoConfiguration(after = {JpaAutoConfig.class})
public class JpaAuditAutoConfig {

    /**
     * Default repository for the audit.
     */
    @SuppressWarnings(SUPPRESS_WARNINGS_INJECTION)
    @Bean
    @ConditionalOnMissingBean
    public AuditRepository defaultAuditRepository(EntityManager entityManager, JpaProperties jpaProperties) {
        return new AuditRepository(entityManager, jpaProperties.getAudit().getSqlCommand());
    }

    /**
     * Listener for the audit event.
     */
    @Bean
    public ApplicationListener<JpaAuditEvent> defaultAuditListener(AuditRepository auditRepository) {
        return event -> {
            auditRepository.executeUpdate();
        };
    }

}