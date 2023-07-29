package com.lsv.lib.spring.security.web.resourceserver.resolver;

import com.lsv.lib.core.function.Resolver;
import com.lsv.lib.security.web.helper.oidc.IssuerHelper;
import com.lsv.lib.security.web.properties.oidc.Issuer;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * Resolver an AuthenticationManager based on the HttpServletRequest.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
@RequiredArgsConstructor
public class AuthenticationManagerResolverByIssuer implements
    AuthenticationManagerResolver<HttpServletRequest>,
    Resolver<HttpServletRequest, AuthenticationManager> {

    private final IssuerHelper issuerHelper;
    private final Resolver<Issuer, JwtDecoder> issuerJwtDecoderResolver;
    private final Resolver<Issuer, Converter<Jwt, AbstractAuthenticationToken>> converterJwtAuthResolver;
    private final Resolver<Issuer, RequestMatcher> requestMatcherIntrospectResolver;
    private final Resolver<AuthenticationManagerDataCache, JwtAuthenticationProvider> jwtAuthenticationProviderResolver;
    private final Resolver<AuthenticationManagerDataCache, OpaqueTokenAuthenticationProvider> opaqueTokenAuthenticationProviderResolver;

    private BearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
    private final Map<Issuer, AuthenticationManagerDataCache> internalCache = new HashMap<>();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Here the solution to work with multi-tenancy is applied, with AuthenticationManagerResolver objects for each issuer.
     * An issuer can have one AuthenticationManager for opaque token (instrospect) and another for jwt,
     * if the instrospect is enabled "and" if only for specific requests.
     */
    @Override
    public AuthenticationManager resolve(HttpServletRequest httpServletRequest) {
        var issuer = resolveIssuer(httpServletRequest);

        var dataCache = internalCache.get(issuer);
        if (dataCache == null) {
            dataCache = new AuthenticationManagerDataCache();
            internalCache.put(issuer, dataCache);

            dataCache.issuer(issuer);
            dataCache.jwtDecoder(issuerJwtDecoderResolver.resolve(issuer));
            dataCache.converterJwtAuth(converterJwtAuthResolver.resolve(issuer));
            dataCache.introspectRequestMatcher(requestMatcherIntrospectResolver.resolve(issuer));
        }

        if (dataCache.introspectRequestMatcher().matches(httpServletRequest)) {
            if (dataCache.providerManagerOpaque() == null) {
                dataCache.providerManagerOpaque(new ProviderManager(
                    opaqueTokenAuthenticationProviderResolver.resolve(dataCache)));
            }

            log.trace("{} - Introspect habilitado para request \"{}\"", issuer.getIssuerUri(), httpServletRequest.getRequestURI());

            return dataCache.providerManagerOpaque();
        } else {
            if (dataCache.providerManagerJwt() == null) {
                dataCache.providerManagerJwt(new ProviderManager(
                    jwtAuthenticationProviderResolver.resolve(dataCache)));
            }

            return dataCache.providerManagerJwt();
        }
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    protected Issuer resolveIssuer(HttpServletRequest httpServletRequest) {
        try {
            return issuerHelper.findIssuer(
                JWTParser
                    .parse(defaultBearerTokenResolver.resolve(httpServletRequest))
                    .getJWTClaimsSet().getIssuer()
            );
        } catch (Exception e) {
            // Converts to a security-specific authentication so that
            // BearerTokenAuthenticationFilter#doFilterInternal can handle and forward it correctly.
            throw new InvalidBearerTokenException(null, e);
        }
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Getter
    @Setter(AccessLevel.PRIVATE)
    public static class AuthenticationManagerDataCache {
        private Issuer issuer;
        private JwtDecoder jwtDecoder;
        private Converter<Jwt, AbstractAuthenticationToken> converterJwtAuth;
        private RequestMatcher introspectRequestMatcher;
        private ProviderManager providerManagerJwt;
        private ProviderManager providerManagerOpaque;
    }
}