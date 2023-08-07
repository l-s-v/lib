package com.lsv.lib.spring.security.web.properties;

import com.lsv.lib.core.audit.AuditProperties;
import com.lsv.lib.core.helper.LibConstants;
import com.lsv.lib.core.properties.LibProperties;
import com.lsv.lib.security.web.properties.Cors;
import com.lsv.lib.security.web.properties.HttpMatcher;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static java.util.Collections.EMPTY_LIST;

/**
 * General web security settings.
 *
 * @author Leandro da Silva Vieira
 */
@Data

@Validated
@ConfigurationProperties(SpringSecurityWebProperties.PATH)
public class SpringSecurityWebProperties implements LibProperties {

    public static final String PATH = LibConstants.BASE_LIB_PROPERTIES + "security.web";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * If false, allow anonymous access to all (/**) urls.
     */
    private boolean enabled = true;
    /**
     * Matchers for anonymously accessible resources.
     */
    @NotNull
    private List<HttpMatcher> httpRequestsPermitAll = EMPTY_LIST;
    /**
     * CORS settings. Default it disabled.
     */
    @NotNull
    private List<Cors> cors = EMPTY_LIST;
    /**
     * Properties related to the audit.
     */
    private AuditProperties audit;
}