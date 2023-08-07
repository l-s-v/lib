package com.lsv.lib.spring.security.web.helper;

import com.lsv.lib.spring.security.web.user.UserSpring;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Provides helper methods for the spring security module.
 *
 * @author Leandro da Silva Vieira
 */
public class SpringSecurityHelper {

    public static UserSpring resolveUser() {
        try{
            return (UserSpring) SecurityContextHolder.getContext().getAuthentication();
        } catch (NullPointerException e) {
            throw new IllegalCallerException("Usuário não autenticado.");
        }
    }
}
