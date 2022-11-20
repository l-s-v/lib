package com.lsv.lib.core.loader;

import java.util.Collection;

public interface Loadable {

    <T> Collection<T> load(Class<T> classType);

    Integer priority();
}