package com.lsv.lib.spring.security.web.user;

import com.lsv.lib.core.security.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Provides a pattern for user objects using spring.
 *
 * @author Leandro da Silva Vieira
 */
public interface UserSpring extends User {

    Collection<GrantedAuthority> getAuthorities();

    default Collection<String> getGrants() {
        return getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toUnmodifiableList());
    };
}