package com.lsv.lib.spring.jpa.audit.event;

import com.lsv.lib.core.event.EventPublisher;
import com.lsv.lib.spring.jpa.audit.event.JpaAuditEvent.JpaAuditManualEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Leandro da Silva Vieira
 */
@Slf4j
public abstract class JpaAuditManual {

    private static final JpaAuditEvent JPA_AUDIT_MANUAL_EVENT = new JpaAuditManualEvent();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static void startAuditTransaction() {
        log.trace("Iniciando manualmente a auditoria da transação");
        EventPublisher.publish(JPA_AUDIT_MANUAL_EVENT);
    }
}