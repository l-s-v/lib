package com.lsv.lib.spring.security.web.resourceserver.user;

import com.lsv.lib.spring.security.web.user.UserSpring;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Map;

/**
 * User implementation to work with JWT
 *
 * @author Leandro da Silva Vieira
 */
public class UserJwt extends JwtAuthenticationToken implements UserSpring {

    public UserJwt(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name) {
        super(jwt, authorities, name);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public Map<String, Object> getAttributes() {
        return super.getTokenAttributes();
    }
}