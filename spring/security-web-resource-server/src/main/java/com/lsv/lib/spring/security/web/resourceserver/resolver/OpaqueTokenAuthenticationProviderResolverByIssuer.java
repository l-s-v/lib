package com.lsv.lib.spring.security.web.resourceserver.resolver;

import com.lsv.lib.core.function.Resolver;
import com.lsv.lib.spring.security.web.resourceserver.resolver.AuthenticationManagerResolverByIssuer.AuthenticationManagerDataCache;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;

/**
 * Resolver an OpaqueTokenAuthenticationProvider based on the AuthenticationManagerResolverByIssuer.DataCache.
 *
 * @author Leandro da Silva Vieira
 */
public class OpaqueTokenAuthenticationProviderResolverByIssuer implements Resolver<AuthenticationManagerDataCache, OpaqueTokenAuthenticationProvider> {

    @Override
    public OpaqueTokenAuthenticationProvider resolve(AuthenticationManagerDataCache authenticationManagerDataCache) {
        var opaqueTokenAuthenticationProvider = new OpaqueTokenAuthenticationProvider(
            new NimbusOpaqueTokenIntrospector(authenticationManagerDataCache.issuer().getEndPoints().getIntrospectionEndpoint(),
                authenticationManagerDataCache.issuer().getClientId(),
                authenticationManagerDataCache.issuer().getClientSecret())
        );
        opaqueTokenAuthenticationProvider.setAuthenticationConverter((introspectedToken, authenticatedPrincipal) ->
            authenticationManagerDataCache.converterJwtAuth().convert(authenticationManagerDataCache.jwtDecoder().decode(introspectedToken)));

        return opaqueTokenAuthenticationProvider;
    }
}