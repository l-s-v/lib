package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.pattern.register.RegisterByInterface;

public interface Service<T extends Identifiable<?>> {

    @SuppressWarnings("unchecked")
    static <S extends Service<?>> S findInstance(Object sourceBase) {
        return (S) RegisterByInterface.findImplementation(
            HelperClass.identifyGenericsClass(sourceBase, Service.class));
    }
}