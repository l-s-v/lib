package com.lsv.lib.spring.security.web.helper;

import com.lsv.lib.core.security.UserHandler;
import com.lsv.lib.spring.security.web.user.UserSpring;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Provides helper methods for the spring security module.
 *
 * @author Leandro da Silva Vieira
 */
@Component
public class SpringSecurityHelper implements UserHandler {

    @Override
    public UserSpring getUser() {
        try{
            return (UserSpring) SecurityContextHolder.getContext().getAuthentication();
        } catch (NullPointerException e) {
            throw new IllegalCallerException("Usuário não autenticado.");
        }
    }
}
