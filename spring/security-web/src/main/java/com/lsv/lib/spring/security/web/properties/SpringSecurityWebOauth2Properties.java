package com.lsv.lib.spring.security.web.properties;

import com.lsv.lib.core.properties.LibProperties;
import com.lsv.lib.security.web.properties.oidc.Issuer;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;

/**
 * Web security settings using the Oauth 2 protocol.
 *
 * @author Leandro da Silva Vieira
 */
@Data

@Validated
@ConfigurationProperties(SpringSecurityWebOauth2Properties.PATH)
public class SpringSecurityWebOauth2Properties extends HashSet<Issuer> implements LibProperties {

    public static final String PATH = SpringSecurityWebProperties.PATH + ".oauth2";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
}