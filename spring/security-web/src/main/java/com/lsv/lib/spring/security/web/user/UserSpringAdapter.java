package com.lsv.lib.spring.security.web.user;

import com.lsv.lib.core.security.UserHandler;
import lombok.experimental.Delegate;

/**
 * Facilitates the adaptation of the logged in user to some specific context.
 *
 * @author Leandro da Silva Vieira
 */
public abstract class UserSpringAdapter {

    @Delegate(types = UserSpring.class)
    private UserSpring getUser() {
        return (UserSpring) UserHandler.resolveUser();
    }
}