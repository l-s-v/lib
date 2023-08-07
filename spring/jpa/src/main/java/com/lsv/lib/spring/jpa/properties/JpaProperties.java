package com.lsv.lib.spring.jpa.properties;

import com.lsv.lib.core.helper.LibConstants;
import com.lsv.lib.core.properties.LibProperties;
import com.lsv.lib.spring.jpa.audit.properties.JpaAuditProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Settings for observability module.
 *
 * @author Leandro da Silva Vieira
 */
@Getter
@Setter

@Validated
@ConfigurationProperties(JpaProperties.PATH)
public class JpaProperties implements LibProperties {

    public static final String PATH = LibConstants.BASE_LIB_PROPERTIES + "database";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Properties related to the audit.
     */
    @Valid
    @NotNull
    private JpaAuditProperties audit = new JpaAuditProperties();
}