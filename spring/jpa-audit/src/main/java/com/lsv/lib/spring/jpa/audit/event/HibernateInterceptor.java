package com.lsv.lib.spring.jpa.audit.event;

import com.lsv.lib.core.event.EventPublisher;
import com.lsv.lib.spring.jpa.audit.event.JpaAuditEvent.JpaAuditTransactionEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Makes the link between the data that can be audited and the transaction in the database:
 *
 * - Uses org.hibernate.Interceptor to be notified when the transaction is opened or closed.
 * - Uses org.hibernate.resource.jdbc.spi.StatementInspector to be notified before a statement is executed.
 *
 * This class must be registered in the hibernate properties.
 * Directly to hibernate properties
 * - current_session_context_class: org.hibernate.context.internal.ThreadLocalSessionContext
 * - hibernate.session_factory.interceptor: {package name}.HibernateInterceptor
 * - hibernate.session_factory.statement_inspector: {package name}.HibernateInterceptor
 *
 * Using spring jpa, just add prefix spring.jpa.properties to application.yaml properties.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
public class HibernateInterceptor implements Interceptor, StatementInspector {

    private static final JpaAuditEvent JPA_AUDIT_TRANSACTION_EVENT = new JpaAuditTransactionEvent();
    private static final String REGEX_INSERT_UPDATE_DELETE = "^\\s*(?i)(insert\\s*into|update|delete\\s*from)";

    private static final ThreadLocal<Boolean> transactionRequiredAudit = new ThreadLocal<>();
    private String regexTables;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public HibernateInterceptor(Set<String> tablesAudited) {
        if (ObjectUtils.isNotEmpty(tablesAudited)) {
            regexTables = "(?i)(%s)".formatted(StringUtils.join(tablesAudited, "|"));
        }
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public void afterTransactionBegin(Transaction tx) {
        log.trace("Interceptado início da transação");
        transactionRequiredAudit.set(false);
    }

    @Override
    public void beforeTransactionCompletion(Transaction tx) {
        log.trace("Interceptado final da transação");

        if (transactionRequiredAudit.get()) {
            EventPublisher.publish(JPA_AUDIT_TRANSACTION_EVENT);
        }

        transactionRequiredAudit.remove();
    }

    @Override
    public String inspect(String sql) {
        log.trace("Interceptado statement com sql [{}]", sql);

        // If the current transaction does not yet require auditing "E"
        // If the sql fits the audit criteria
        if (!transactionRequiredAudit.get() && match(sql)) {
            log.trace("Transação atual requer auditoria");
            transactionRequiredAudit.set(true);
        }

        return sql;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private boolean match(String sql) {
        // If it is a sql that modifies the database
        if (!Pattern.compile(REGEX_INSERT_UPDATE_DELETE).matcher(sql).find()) {
            return false;
        }
        // If specific tables were informed to audit
        return ObjectUtils.isEmpty(regexTables) || Pattern.compile(regexTables).matcher(sql).find();
    }
}