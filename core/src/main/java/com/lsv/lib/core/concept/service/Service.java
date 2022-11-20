package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.loader.Loader;

public interface Service<T extends Identifiable<?>> {

    static <ID extends Identifiable<?>> Service<ID> findInstance(Object sourceBase, Class<?> sourceExtends) {
        return (Service<ID>) Loader.findImplementation(HelperClass.identifyGenericsClass(sourceBase, sourceExtends));
    }
}