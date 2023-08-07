package com.lsv.lib.spring.jpa.audit.event;

import com.lsv.lib.core.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;

import java.io.Serializable;

import static com.lsv.lib.spring.jpa.audit.event.JpaAuditEvent.JPA_AUDIT_EVENT;

/**
 * Makes the link between the data that can be audited and the transaction in the database:
 *  - Uses org.hibernate.Interceptor to be notified when the transaction is opened.
 *
 * This class must be registered in the hibernate properties.
 * Directly to hibernate properties
 * - current_session_context_class: org.hibernate.context.internal.ThreadLocalSessionContext
 * - hibernate.session_factory.interceptor: {package name}.HibernateTransactionInterceptor
 *
 * Using spring jpa, just add spring.jpa.properties prefix to application.yaml properties.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
public class HibernateTransactionInterceptor implements Interceptor, Serializable {

    @Override
    public void afterTransactionBegin(Transaction tx) {
        log.trace("Interceptado início da transação");
        EventPublisher.publish(JPA_AUDIT_EVENT);
    }
}