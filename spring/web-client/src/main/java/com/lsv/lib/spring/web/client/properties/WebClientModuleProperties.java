package com.lsv.lib.spring.web.client.properties;

import com.lsv.lib.core.helper.LibConstants;
import com.lsv.lib.core.properties.LibProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Settings for web client module.
 *
 * @author Leandro da Silva Vieira
 */
@Getter
@Setter

@Validated
@ConfigurationProperties(WebClientModuleProperties.PATH)
public class WebClientModuleProperties implements LibProperties {

    public static final String PATH = LibConstants.BASE_LIB_PROPERTIES + "web.client";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Base packages to scan for annotated components.
     */
    @NotNull
    private List<String> basePackages = Collections.emptyList();
    /**
     * It allows defining a default configuration that can be used when some value of the configurations is not filled.
     */
    @NestedConfigurationProperty
    private WebClientProperties defaultConfig = new WebClientProperties();
    /**
     * Configurations for running a HttpExchangeClient.
     */
    @NestedConfigurationProperty
    private Map<String, WebClientProperties> configurations = new HashMap<>();
}