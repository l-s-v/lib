package com.lsv.lib.core.security;

import com.lsv.lib.core.loader.Loader;

/**
 * Sets the default for objects that handler the user.
 *
 * @author Leandro da Silva Vieira
 */
public interface UserHandler {

    User getUser();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    UserHandler USER_HANDLER = Loader.of(UserHandler.class).findUniqueImplementationByFirstLoader();

    static User resolveUser() {
        return USER_HANDLER.getUser();
    }
}