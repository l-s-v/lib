package com.lsv.lib.spring.security.web.client.resolver;

import com.lsv.lib.spring.security.web.client.user.UserOidc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;

/**
 * Converts a spring Oauth2 authentication object to UserOidc.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
@RequiredArgsConstructor
public class ConverterOAuth2UserOidcResolver implements Converter<OAuth2LoginAuthenticationToken, OAuth2AuthenticationToken> {

    @Override
    public OAuth2AuthenticationToken convert(OAuth2LoginAuthenticationToken authenticationResult) {
        log.trace("Convertendo usuário {} para UserOidc", authenticationResult.getClass().getSimpleName());
        return new UserOidc(authenticationResult.getPrincipal(), authenticationResult.getAuthorities(),
            authenticationResult.getClientRegistration().getRegistrationId());
    }
}