package com.lsv.lib.spring.security.web.client.user;

import com.lsv.lib.spring.security.web.user.UserSpring;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * User implementation to work with OIDC (client oauth2)
 *
 * @author Leandro da Silva Vieira
 */
public class UserOidc extends OAuth2AuthenticationToken implements UserSpring {

    public UserOidc(OAuth2User principal, Collection<? extends GrantedAuthority> authorities, String authorizedClientRegistrationId) {
        super(principal, authorities, authorizedClientRegistrationId);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public Map<String, Object> getAttributes() {
        return super.getPrincipal().getAttributes();
    }
}