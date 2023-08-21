package com.lsv.lib.spring.web.client.filter;

import com.lsv.lib.security.web.properties.oidc.ServiceAccount;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ClientCredentialsReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;

/**
 * Creates the filter used for automatic authentication using service account (client_credentials).
 *
 * @author Leandro da Silva Vieira
 */
public class OAuth2ClientCredentialsFilter {

    public static ExchangeFilterFunction create(String id, ServiceAccount serviceAccount) {
        var clientRegistrationRepository = new InMemoryReactiveClientRegistrationRepository(
            ClientRegistration.withRegistrationId(id)
                .authorizationGrantType(CLIENT_CREDENTIALS)
                .clientId(serviceAccount.getClientId())
                .clientSecret(serviceAccount.getClientSecret())
                .tokenUri(serviceAccount.getTokenUri())
                .scope(serviceAccount.getScope())
                .build());

        var authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository,
            new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository));
        authorizedClientManager.setAuthorizedClientProvider(new ClientCredentialsReactiveOAuth2AuthorizedClientProvider());

        var oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultClientRegistrationId(id);

        return oauth;
    }
}