package com.lsv.lib.spring.web.resilient4j.properties;

import com.lsv.lib.core.helper.LibConstants;
import com.lsv.lib.core.properties.LibProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

/**
 * Settings for resilient4j module.
 *
 * @author Leandro da Silva Vieira
 */
@Getter
@Setter

@Validated
@ConfigurationProperties(R4jModuleProperties.PATH)
public class R4jModuleProperties implements LibProperties {

    public static final String PATH = LibConstants.BASE_LIB_PROPERTIES + "web.resilient4j";

    public static final String PROP_ENABLED = "enabled";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Enables all resilient4j settings.
     */
    private boolean enabled;
    /**
     * It allows defining a default configuration that can be used when some value of the configurations is not filled.
     */
    @NestedConfigurationProperty
    private R4jProperties defaultConfig;
    /**
     * Settings for the requests.
     */
    @NestedConfigurationProperty
    private List<R4jProperties> configurations = new LinkedList<>();

    /**
     * Cache settings that store resilient4j operators that are linked to a given event (class, http method and uri).
     */
    private Cache cacheOperator = new Cache();
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Data
    public static class Cache {
        /**
         * Maximum number of objects stored.
         */
        @NotNull
        private Integer size;
        /**
         * Maximum storage time since last access.
         */
        @NotNull
        private Duration duration;
    }
}