package com.lsv.lib.security.web.properties.oidc;

import com.lsv.lib.security.web.properties.HttpMatcher;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Issuer for OAuth 2.0.
 *
 * @author Leandro da Silva Vieira
 */
@Data
@EqualsAndHashCode(of = "issuerUri")
public class Issuer {

    /**
     * URI that can either be an OpenID Connect discovery endpoint or an OAuth 2.0
     * Authorization Server Metadata endpoint defined by RFC 8414.
     */
    @NotBlank
    private String issuerUri;
    /**
     * client_id for OAuth 2.0.
     * When in client mode, used for the registration.
     * When in resource server mode, used to authenticate with the token introspection endpoint.
     */
    private String clientId;
    /**
     * client_secret for OAuth 2.0.
     * When in client mode, used for the registration.
     * When in resource server mode, used to authenticate with the token introspection endpoint.
     */
    private String clientSecret;
    /**
     * Name of the attribute that will be used to extract the username.
     * Default is preferred_username.
     */
    @NotBlank
    private String userNameAttribute = "preferred_username";
    /**
     * Enables JWT token validation as a resource server. Por padrão é habilitado.
     * Requires registered resource server library.
     */
    private boolean enabledResourceServer = true;
    /**
     * Only used in resource server mode, to enable token validation using introspect (opaque token).
     * Requires registered resource server library.
     * Se habilitado, também habilita enabledResourceServer.
     */
    private boolean enabledIntrospect;
    /**
     * If enabled, it will direct to the issuer authentication screen.
     * Requires registered client library.
     */
    private boolean enabledClient;
    /**
     * Used in client mode only, to define which authorization scopes to request.
     * When left blank the provider's default scopes, if any, will be used.
     */
    private Set<String> scopes = Set.of(EndPoints.OPENID_ESCOPE_OPEN_ID);
    /**
     * Configurations available for recovering authorities.
     * Default is REALM_ACESS_OAUTH2:
     * - path: $.realm_access.roles
     * - pattern: ROLE_%
     * - transform: Transform.UPPER
     */
    private Set<Authority> authorities = new HashSet<>(Set.of(Authority.REALM_ACCESS_OAUTH2));
    /**
     * Issuer specific settings.
     * By default, use openId Connect.
     */
    @NotNull
    private EndPoints endPoints = new EndPoints();
    /**
     * Matchers that require token introspect.
     * If set, will also enable enabledIntrospect.
     */
    @NotNull
    private List<HttpMatcher> httpRequestsIntrospect = Collections.EMPTY_LIST;
    /**
     * Refinement with extra settings.
     */
    @NotNull
    private ExtraConfig extraConfig = new ExtraConfig();
    /**
     * Allows you to disable this authentication (resource server) for use in the document api (swagger).
     * Default it is active, but only if enabledResourceServer=true.
     */
    private boolean enabledResourceServerForApiDocs = true;
    /**
     * Allows you to disable this authentication (client) for use in the document api (swagger).
     * Default it is active, but only if enabledClient=true.
     */
    private boolean enabledClientForApiDocs = true;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// Validations

    @AssertFalse(message = "clientId é obrigatório para habilitar cliente")
    private boolean isClientIdRequiredIfEnabledClient() {
        return isEnabledClient() && ObjectUtils.isEmpty(getClientId());
    }

    @AssertFalse(message = "clientId e clientSecret são obrigatórios para habilitar introspect")
    private boolean isClientIdAndSecretRequiredsIfEnableIntrospect() {
        return this.isEnabledIntrospect() && (ObjectUtils.isEmpty(getClientId()) || ObjectUtils.isEmpty(getClientSecret()));
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Defines that Roles are also loaded from resources.
     * For that, add RESOURCE_ACESS_OAUTH2 to the authorities
     * * - path: $.resource_access.*.roles
     * * - pattern: ROLE_%
     * * - transform: Transform.UPPER
     */
    public void setUseResourceRoles(boolean useResourceRoles) {
        if (useResourceRoles) {
            authorities.add(Authority.RESOURCE_ACCESS_OAUTH2);
        }
    }

    /**
     * Defines that the roles are also loaded from the realm.
     * By default they will already be added, but this property makes it easier to remove this configuration.
     */
    public void setUseRealmRoles(boolean useRealmRoles) {
        if (!useRealmRoles) {
            authorities.remove(Authority.REALM_ACCESS_OAUTH2);
        }
    }

    /**
     * Using introspect also enables resource server.
     */
    public void setEnabledIntrospect(boolean enabledIntrospect) {
        this.enabledIntrospect = enabledIntrospect;

        if (enabledIntrospect) {
            setEnabledResourceServer(true);
        }
    }

    /**
     * Ensures settings are aligned with the issuer.
     */
    public void setIssuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
        this.getEndPoints().setIssuer(issuerUri);
    }

    public void setHttpRequestsIntrospect(List<HttpMatcher> httpRequestsIntrospect) {
        if(ObjectUtils.isNotEmpty(httpRequestsIntrospect)) {
            setEnabledIntrospect(true);
        }
        this.httpRequestsIntrospect = httpRequestsIntrospect;
    }
}