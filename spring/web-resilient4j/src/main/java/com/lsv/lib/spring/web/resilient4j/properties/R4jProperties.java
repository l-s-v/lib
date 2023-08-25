package com.lsv.lib.spring.web.resilient4j.properties;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpMethod;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Settings for resilient4j.
 *
 * @author Leandro da Silva Vieira
 */
@Data
public class R4jProperties {

    /**
     * Defines for which requests this configuration will be applied.
     * Define WHEN.
     */
    private Match match = new Match();
    /**
     * Defines the Resilience4J settings that must be applied in the request.
     * Define WHAT.
     */
    private Set<R4jInstance> instances = new LinkedHashSet<>();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Data
    public static class Match {
        /**
         * Base package of the class where the call originated.
         * The comparison uses {className}.startsWith(packageName.getName()).
         */
        private String packageName;
        /**
         * Class where the call originated.
         * Must not be used together with packageName.
         */
        private Class<?> clientClass;
        /**
         * HTTP method of the request.
         */
        private HttpMethod httpMethod;
        /**
         * URI definido no método http.
         */
        private String uri;

        // Validations
        @AssertFalse(message = "clientClass e packageName não devem ser informados ao mesmo tempo")
        public boolean isPackageNameAndClientClass() {
            return ObjectUtils.allNotNull(this.getPackageName(), this.getClientClass());
        }
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Just filling the object taking advantage of the method that will be automatically called.
     */
    @AssertTrue
    public boolean isSetList() {
        instances.forEach(r4jInstance -> r4jInstance.r4jProperties(this));
        return true;
    }
}