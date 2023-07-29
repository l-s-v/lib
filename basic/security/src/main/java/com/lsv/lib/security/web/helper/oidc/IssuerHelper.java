package com.lsv.lib.security.web.helper.oidc;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.lsv.lib.security.web.properties.oidc.Authority;
import com.lsv.lib.security.web.properties.oidc.Issuer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Leandro da Silva Vieira
 */
@Slf4j
@Getter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class IssuerHelper {

    private final Collection<Issuer> issuers;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public boolean hasIssuers() {
        return !issuers().isEmpty();
    }

    public boolean hasIssuerClients() {
        return !filterIssuerClients().isEmpty();
    }

    public boolean hasIssuerResourceServers() {
        return !filterIssuerResourceServers().isEmpty();
    }

    public List<Issuer> filterIssuerClients() {
        return filterIssuer(issuer -> issuer.isEnabledClient());
    }

    public List<Issuer> filterIssuerResourceServers() {
        return filterIssuer(issuer -> issuer.isEnabledResourceServer());
    }

    public List<Issuer> filterIssuerClientsApiDocs() {
        return filterIssuer(issuer -> issuer.isEnabledClient() && issuer.isEnabledClientForApiDocs());
    }

    public List<Issuer> filterIssuerResourceServersApiDocs() {
        return filterIssuer(issuer -> issuer.isEnabledResourceServer() && issuer.isEnabledResourceServerForApiDocs());
    }

    public Issuer findIssuerByClaims(Map<String, Object> claims) {
        return findIssuer(claims.get(ConstantsWebOidc.CLAIM_TOKEN_ISS).toString());
    }

    public Issuer findIssuer(String issuerUri) {
        return issuers().stream()
            .filter(issuer -> issuer.getIssuerUri().equals(issuerUri))
            .findFirst()
            .orElseThrow(() ->
                new NoSuchElementException("Não foi encontrada configuração para issuer %s".formatted(issuerUri)));
    }

    public <R> Collection<R> extractRoles(Issuer issuer, Map<String, Object> claims, Function<String, ? extends R> mapper) {
        return issuer
            .getAuthorities().stream()
            .flatMap(authority -> extractRolesByAuthorityConfig(authority, claims))
            .map(mapper)
            .collect(Collectors.toSet());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private List<Issuer> filterIssuer(Predicate<Issuer> filter) {
        return issuers().stream()
            .filter(filter)
            .collect(Collectors.toUnmodifiableList());
    }

    private Stream<String> extractRolesByAuthorityConfig(Authority authority, Map<String, Object> data) {
        return extractRolesByPath(authority.getPath(), data)
            .map(StringUtils::trimToNull)
            .filter(Objects::nonNull)
            .map(role -> transform(authority.getTransform(), role))
            .map(s -> authority.getPattern().formatted(s));
    }

    private Stream<String> extractRolesByPath(String path, Map<String, Object> data) {
        try {
            var value = JsonPath.read(data, path);
            if (value instanceof String s) {
                return Stream.of(s);
            }
            if (value instanceof List l) {
                if (l.isEmpty()) {
                    return Stream.empty();
                }
                if (l.get(0) instanceof String) {
                    return l.stream();
                }
                if (l.get(0) instanceof List) {
                    return l.stream().flatMap(l2 -> ((List) l2).stream());
                }
            }

            return Stream.empty();
        } catch (PathNotFoundException e) {
            log.debug("Pattern '%s' não encontrado.".formatted(path));
            return Stream.empty();
        }
    }

    private String transform(Authority.Transform transform, String value) {
        return switch (transform) {
            case UPPER -> value.toUpperCase();
            case LOWER -> value.toLowerCase();
            default -> value;
        };
    }
}