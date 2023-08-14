package com.lsv.lib.spring.jpa.audit.event;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEvent;

/**
 * @author Leandro da Silva Vieira
 */
public abstract class JpaAuditEvent extends ApplicationEvent {

    public JpaAuditEvent() {
        super(StringUtils.EMPTY);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static class JpaAuditTransactionEvent extends JpaAuditEvent {}
    public static class JpaAuditManualEvent extends JpaAuditEvent {}
}