package com.lsv.lib.spring.web.properties;

import com.lsv.lib.core.helper.LibConstants;
import com.lsv.lib.core.properties.LibProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Settings for swagger.
 *
 * @author Leandro da Silva Vieira
 */
@Data

@Validated
@ConfigurationProperties(SpringWebProperties.PATH)
public class SpringWebProperties implements LibProperties {

    public static final String PATH = LibConstants.BASE_LIB_PROPERTIES + "web";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
}