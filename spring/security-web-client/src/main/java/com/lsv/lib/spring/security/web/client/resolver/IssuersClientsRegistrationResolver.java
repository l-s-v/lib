package com.lsv.lib.spring.security.web.client.resolver;

import com.lsv.lib.core.function.Resolver;
import com.lsv.lib.security.web.properties.oidc.Issuer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;

/**
 * Resolve the library-specific properties to the original spring security ClientRegistration.
 *
 * @author Leandro da Silva Vieira
 */
public class IssuersClientsRegistrationResolver implements Resolver<Collection<Issuer>, Map<String, ClientRegistration>> {

    private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public Map<String, ClientRegistration> resolve(Collection<Issuer> issuers) {
        var registrations = new HashMap<String, ClientRegistration>();

        issuers.stream()
            .forEach(issuer -> {
                var key = formKey(issuer.getIssuerUri());

                // It creates the ClientRegistration directly because if you use the Builders to create it
                // through the uriIssuer, it tries to load the configurations when starting the application
                // and it won't work if the issuer's server is not accessible.
                registrations.put(key, ClientRegistration
                    .withRegistrationId(key)
                    // Internal config
                    .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                    .authorizationGrantType(new AuthorizationGrantType(issuer.getEndPoints().getGrantTypesSupported()))
                    .redirectUri(DEFAULT_REDIRECT_URL)
                    // Registration
                    .clientId(issuer.getClientId())
                    .clientSecret(issuer.getClientSecret())
                    .userNameAttributeName(issuer.getUserNameAttribute())
                    .scope(issuer.getScopes())
                    // Provider
                    .issuerUri(issuer.getIssuerUri())
                    .authorizationUri(issuer.getEndPoints().getAuthorizationEndpoint())
                    .tokenUri(issuer.getEndPoints().getTokenEndpoint())
                    .userInfoUri(issuer.getEndPoints().getUserInfoEndpoint())
                    .jwkSetUri(issuer.getEndPoints().getJwksUri())

                    .build()
                );
            });

        return registrations;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private String formKey(String issuerUri) {
        return Base64.getEncoder().encodeToString(issuerUri.getBytes());
    }
}