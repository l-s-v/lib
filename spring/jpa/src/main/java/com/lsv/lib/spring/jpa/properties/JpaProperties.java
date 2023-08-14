package com.lsv.lib.spring.jpa.properties;

import com.lsv.lib.core.helper.LibConstants;
import com.lsv.lib.core.properties.LibProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Settings for observability module.
 *
 * @author Leandro da Silva Vieira
 */
@Getter
@Setter
@Accessors(fluent = false)

@Validated
@ConfigurationProperties(JpaProperties.PATH)
public class JpaProperties implements LibProperties {

    public static final String PATH = LibConstants.BASE_LIB_PROPERTIES + "database";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
}