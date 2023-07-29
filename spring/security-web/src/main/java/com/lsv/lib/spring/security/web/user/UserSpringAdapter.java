package com.lsv.lib.spring.security.web.user;

import lombok.experimental.Delegate;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Facilitates the adaptation of the logged in user to some specific context.
 *
 * @author Leandro da Silva Vieira
 */
public abstract class UserSpringAdapter {

    @Delegate(types = UserSpring.class)
    private UserSpring getUser() {
        return resolveUser();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static UserSpring resolveUser() {
        try{
            return (UserSpring) SecurityContextHolder.getContext().getAuthentication();
        } catch (NullPointerException e) {
            throw new IllegalCallerException("Usuário não autenticado.");
        }
    }
}