package com.lsv.lib.spring.security.web.client.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;
import java.util.Map;

/**
 * Overrides DefaultOidcUser to allow storing AccessToken attributes and not just IdToken attributes.
 *
 * @author Leandro da Silva Vieira
 */
@Accessors(fluent = false)
@Getter
@Setter(AccessLevel.PRIVATE)
public class UserOidcAcessToken extends DefaultOidcUser {

    private Map<String, Object> attributesAccessToken;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public UserOidcAcessToken(Collection<? extends GrantedAuthority> authorities,
                              OidcIdToken idToken,
                              OidcUserInfo userInfo,
                              String nameAttributeKey,
                              Map<String, Object> attributesAccessToken) {

        super(authorities, idToken, userInfo, nameAttributeKey);
        setAttributesAccessToken(attributesAccessToken);
    }
}