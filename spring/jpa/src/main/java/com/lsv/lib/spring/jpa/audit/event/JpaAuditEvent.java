package com.lsv.lib.spring.jpa.audit.event;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEvent;

/**
 * @author Leandro da Silva Vieira
 */
public class JpaAuditEvent extends ApplicationEvent {

    public static final JpaAuditEvent JPA_AUDIT_EVENT = new JpaAuditEvent(StringUtils.EMPTY);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public JpaAuditEvent(Object source) {
        super(source);
    }
}