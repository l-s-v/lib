package com.lsv.lib.spring.web.resilient4j.properties;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Identifier of a Resilience4J instance.
 *
 * @author Leandro da Silva Vieira
 */
@Data
@EqualsAndHashCode(of = "type")
@ToString(of = {"name", "type"})
public class R4jInstance {
    /**
     * Name of the configuration that should be used. If not informed, it will use the default configuration.
     */
    private String name;
    /**
     * Type of Resilience4J treatment that should be applied.
     */
    @NotNull
    private R4jType type;
    /**
     * It makes it easy to know where the configuration came from.
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private R4jProperties r4jProperties;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// Remove the "get" and "set" so that it is not displayed in the yaml

    public void r4jProperties(R4jProperties r4jProperties) {
        this.r4jProperties = r4jProperties;
    }

    public R4jProperties r4jProperties() {
        return r4jProperties;
    }
}
