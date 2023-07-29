package com.lsv.lib.spring.security.web.resourceserver.resolver;

import com.lsv.lib.core.function.Resolver;
import com.lsv.lib.security.web.properties.oidc.Issuer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * Resolver an JwtDecoder based on the issuer.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
@RequiredArgsConstructor
public class JwtDecoderResolverByIssuer implements Resolver<Issuer, JwtDecoder> {

    private final Resolver<Issuer, Cache> cacheJwksResolverByIssuer;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Defines the main spring object to work with jwt.
     * It is responsible for converting the token (string) into a JWT object, performing the proper validations
     * (signature, expiration, algorithms, etc.), configuring JWKS cache patterns, communicating with the issuer, etc.
     * When necessary to modify the defaults, just create a new bean.
     */
    @Override
    public JwtDecoder resolve(Issuer issuer) {
        // It does not directly use JwtDecoders#fromIssuerLocation to allow customizing the jwtDecoder
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
            .withIssuerLocation(issuer.getIssuerUri())
            .cache(cacheJwksResolverByIssuer.resolve(issuer))
            .build();

        jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer.getIssuerUri()));
        return jwtDecoder;
    }
}