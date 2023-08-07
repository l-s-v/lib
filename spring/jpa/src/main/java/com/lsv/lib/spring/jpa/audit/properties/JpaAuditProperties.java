package com.lsv.lib.spring.jpa.audit.properties;

import com.lsv.lib.core.audit.AuditProperties;
import jakarta.validation.constraints.AssertFalse;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Settings for observability module.
 *
 * @author Leandro da Silva Vieira
 */
@Data
@Accessors(fluent = false)
public class JpaAuditProperties extends AuditProperties {

    /**
     * SQL command (for executeUpdate) that should be executed.
     * Will send the information collected from the application.
     * It must have a single parameter (using :p).
     */
    private String sqlCommand;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// Validations

    @AssertFalse(message = "sqlCommand é obrigatório se enabled=true")
    private boolean isSqlCommandRequiredIfEnabled() {
        return isEnabled() && ObjectUtils.isEmpty(getSqlCommand());
    }
}