package com.lsv.lib.spring.security.web.resourceserver.resolver;

import com.lsv.lib.core.function.Resolver;
import com.lsv.lib.spring.security.web.resourceserver.resolver.AuthenticationManagerResolverByIssuer.AuthenticationManagerDataCache;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

/**
 * Resolver an JwtAuthenticationProvider based on the AuthenticationManagerResolverByIssuer.DataCache.
 *
 * @author Leandro da Silva Vieira
 */
public class JwtAuthenticationProviderResolverByIssuer implements Resolver<AuthenticationManagerDataCache, JwtAuthenticationProvider> {

    @Override
    public JwtAuthenticationProvider resolve(AuthenticationManagerDataCache authenticationManagerDataCache) {
        var jwtAuthenticationProvider = new JwtAuthenticationProvider(authenticationManagerDataCache.jwtDecoder());
        jwtAuthenticationProvider.setJwtAuthenticationConverter(authenticationManagerDataCache.converterJwtAuth());

        return jwtAuthenticationProvider;
    }
}