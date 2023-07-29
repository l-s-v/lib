package com.lsv.lib.spring.security.web.resourceserver.resolver;

import com.lsv.lib.core.function.Resolver;
import com.lsv.lib.security.web.properties.oidc.Issuer;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

/**
 * Resolver an RequestMatcher based on the issuer.
 *
 * @author Leandro da Silva Vieira
 */
public class RequestMatcherIntrospectResolverByIssuer implements Resolver<Issuer, RequestMatcher> {

    @Override
    public RequestMatcher resolve(Issuer issuer) {
        RequestMatcher requestMatcher = request -> issuer.isEnabledIntrospect();

        // If instrospect is not enabled or has no request configured
        return !issuer.isEnabledIntrospect() || ObjectUtils.isEmpty(issuer.getHttpRequestsIntrospect())
            ? requestMatcher
            : new AndRequestMatcher(requestMatcher,
            new OrRequestMatcher(issuer.getHttpRequestsIntrospect().stream()
                .flatMap(matcher -> Arrays.stream(matcher.getPattern())
                    .map(pattern -> createIntrospectRequestMatcher(matcher.getMethod() != null ? HttpMethod.valueOf(matcher.getMethod()) : null, pattern)))
                .toList()
            )
        );
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    protected RequestMatcher createIntrospectRequestMatcher(HttpMethod method, String pattern) {
        if (ObjectUtils.isEmpty(method)) {
            return AntPathRequestMatcher.antMatcher(pattern);
        }
        if (ObjectUtils.isEmpty(pattern)) {
            return AntPathRequestMatcher.antMatcher(method);
        }
        return AntPathRequestMatcher.antMatcher(method, pattern);
    }
}