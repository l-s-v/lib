package com.lsv.lib.spring.security.web.resourceserver.resolver;

import com.lsv.lib.core.function.Resolver;
import com.lsv.lib.security.web.helper.oidc.IssuerHelper;
import com.lsv.lib.security.web.properties.oidc.Issuer;
import com.lsv.lib.spring.security.web.resourceserver.user.UserJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Resolver an Converter<Jwt, AbstractAuthenticationToken> based on the issuer.
 *
 * @author Leandro da Silva Vieira
 */
@RequiredArgsConstructor
public class ConverterJwtAuthResolverByIssuer implements Resolver<Issuer, Converter<Jwt, AbstractAuthenticationToken>> {

    private final IssuerHelper issuerHelper;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Converter for the received JWT.
     * It prepares the data of the logged-in user, including loading the roles according to the settings,
     * and the field defined as the userNameAttribute.
     */
    @Override
    public Converter<Jwt, AbstractAuthenticationToken> resolve(Issuer issuer) {
        return jwt -> new UserJwt(jwt,
            issuerHelper.extractRoles(issuer, jwt.getClaims(), SimpleGrantedAuthority::new),
            String.valueOf(jwt.getClaims().get(issuer.getUserNameAttribute()))
        );
    }
}