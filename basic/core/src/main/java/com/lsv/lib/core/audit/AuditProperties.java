package com.lsv.lib.core.audit;

import com.lsv.lib.core.properties.LibProperties;
import lombok.Data;

/**
 * Common properties for modules that want to use auditing.
 *
 * @author Leandro da Silva Vieira
 */
@Data
public class AuditProperties implements LibProperties {

    public static final String PATH = "audit";
    public static final String PROP_ENABLED = "enabled";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Whether auditing is enabled.
     * It will depend on the context of the sub-module.
     */
    private boolean enabled = true;
}