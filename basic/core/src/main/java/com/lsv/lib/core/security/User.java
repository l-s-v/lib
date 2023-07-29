package com.lsv.lib.core.security;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

/**
 * Defines a standard for working with users, without relying on frameworks.
 *
 * @author Leandro da Silva Vieira
 */
public interface User extends Principal {

    String getName();

    Map<String, Object> getAttributes();

    Collection<String> getGrants();

    default <A> A getAttribute(String key) {
        return (A) getAttributes().get(key);
    }
}