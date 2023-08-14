package com.lsv.lib.spring.jpa.audit.config;

import com.lsv.lib.core.audit.AuditProperties;
import com.lsv.lib.spring.core.annotation.ConditionalAuditEnable;
import com.lsv.lib.spring.core.annotation.YamlSource;
import com.lsv.lib.spring.core.config.SpringCoreAutoConfig;
import com.lsv.lib.spring.jpa.audit.event.HibernateInterceptor;
import com.lsv.lib.spring.jpa.audit.event.JpaAuditEvent;
import com.lsv.lib.spring.jpa.audit.event.JpaAuditEvent.JpaAuditManualEvent;
import com.lsv.lib.spring.jpa.audit.event.JpaAuditEvent.JpaAuditTransactionEvent;
import com.lsv.lib.spring.jpa.audit.properties.JpaAuditProperties;
import com.lsv.lib.spring.jpa.audit.repository.AuditRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.lsv.lib.spring.core.helper.ConstantsSpring.SUPPRESS_WARNINGS_INJECTION;
import static com.lsv.lib.spring.jpa.audit.properties.JpaAuditProperties.PROP_ENABLED_MANUAL;
import static com.lsv.lib.spring.jpa.audit.properties.JpaAuditProperties.PROP_ENABLED_TRANSACTIONS;

/**
 * Autoconfiguration for the jpa audit module.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j

@YamlSource("classpath:/jpa-audit.yaml")
@EnableConfigurationProperties(JpaAuditProperties.class)
@ConditionalAuditEnable(value = JpaAuditProperties.PATH, name = AuditProperties.PROP_ENABLED)
@AutoConfiguration(after = {SpringCoreAutoConfig.class})
public class JpaAuditAutoConfig {


    /**
     * Default repository for the audit.
     */
    @Bean
    @ConditionalOnMissingBean
    public AuditRepository defaultAuditRepository(@SuppressWarnings(SUPPRESS_WARNINGS_INJECTION) EntityManager entityManager,
                                                  JpaAuditProperties jpaAuditProperties) {
        return new AuditRepository(entityManager, jpaAuditProperties.getSqlCommand());
    }

    /**
     * Configuring the interceptor to monitor executed transactions and queries.
     * Definitions of when transaction auditing will start.
     */
    @Bean
    @ConditionalOnEnabledAudit(PROP_ENABLED_TRANSACTIONS)
    public HibernatePropertiesCustomizer defaultHibernateInterceptor(JpaAuditProperties jpaAuditProperties) {
        log.trace("Habilitada auditoria em transações");

        var hibernateInterceptor = new HibernateInterceptor(jpaAuditProperties.getTablesAudited());

        return hibernateProperties -> {
            hibernateProperties.put("hibernate.current_session_context_class", "org.hibernate.context.internal.ThreadLocalSessionContext");
            hibernateProperties.put("hibernate.session_factory.interceptor", hibernateInterceptor);
            hibernateProperties.put("hibernate.session_factory.statement_inspector", hibernateInterceptor);
        };
    }

    /**
     * Listener for the audit automatic event.
     */
    @Bean
    @ConditionalOnEnabledAudit(PROP_ENABLED_TRANSACTIONS)
    public ApplicationListener<JpaAuditTransactionEvent> defaultJpaAuditAutomaticListener(AuditRepository auditRepository) {
        return createListenerJpaAuditEvent("JpaAuditTransactionEvent", auditRepository);
    }

    /**
     * Listener for the audit manual event.
     */
    @Bean
    @ConditionalOnEnabledAudit(PROP_ENABLED_MANUAL)
    public ApplicationListener<JpaAuditManualEvent> defaultJpaAuditManualListener(AuditRepository auditRepository) {
        return createListenerJpaAuditEvent("JpaAuditManualEvent", auditRepository);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private <T extends JpaAuditEvent> ApplicationListener<T> createListenerJpaAuditEvent(String eventName, AuditRepository auditRepository) {
        log.trace("Configurado listener de {}", eventName);
        return event -> execJpaAudiEvent(auditRepository);
    }

    private void execJpaAudiEvent(AuditRepository auditRepository) {
        auditRepository.executeUpdate();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// Local conditionals

    @Retention(RetentionPolicy.RUNTIME)
    @ConditionalOnProperty(
        prefix = JpaAuditProperties.PATH,
        havingValue = "true"
    )
    @interface ConditionalOnEnabledAudit {

        @AliasFor(annotation = ConditionalOnProperty.class, attribute = "name")
        String value();
    }
}