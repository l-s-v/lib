package com.lsv.lib.core.loader;

import java.util.Collection;

/**
 * @author Leandro da Silva Vieira
 */
public interface Loadable {

    <T> Collection<T> load(Class<T> classType);
}