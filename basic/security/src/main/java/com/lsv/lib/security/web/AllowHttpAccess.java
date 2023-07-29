package com.lsv.lib.security.web;

import com.lsv.lib.security.web.properties.HttpMatcher;

import java.util.List;

/**
 * Allows it to be possible to release (allowAll) some access without the need for security dependencies in the project.
 *
 * @author Leandro da Silva Vieira
 */
@FunctionalInterface
public interface AllowHttpAccess {

    List<HttpMatcher> get();
}