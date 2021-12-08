package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.pattern.register.RegisterByInterface;

public interface Repository<T extends Identifiable<?>> {

    @SuppressWarnings("unchecked")
    static <R extends Repository<?>> R findInstance(Object sourceBase) {
        return (R) RegisterByInterface.findImplementation(
            HelperClass.identifyGenericsClass(sourceBase, Repository.class));
    }
}