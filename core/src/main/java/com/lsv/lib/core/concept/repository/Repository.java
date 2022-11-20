package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.loader.Loader;

public interface Repository<T extends Identifiable<?>> {

    static <ID extends Identifiable<?>> Repository<ID> findInstance(Object sourceBase, Class<?> sourceExtends) {
        return (Repository<ID>) Loader.findImplementation(HelperClass.identifyGenericsClass(sourceBase, sourceExtends));
    }
}