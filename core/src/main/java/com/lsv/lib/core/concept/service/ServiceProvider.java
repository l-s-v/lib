package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.Validable;
import com.lsv.lib.core.concept.service.validations.ValidableIdentifiable;
import com.lsv.lib.core.pattern.register.RegisterByInterface;

import java.util.List;

public interface ServiceProvider<
    I extends Identifiable<?>,
    R extends Repository<I>> {

    List<Validable<?>> VALIDABLES = List.of(ValidableIdentifiable.<Identifiable<?>>of().get());

    R repository();

    List<Validable<I>> validables();

    ServiceProvider<?, ?> configureRequiredWhenByService(Object sourceBase);
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    static <
        I extends Identifiable<?>,
        R extends Repository<I>>
    ServiceProvider<I, R> findInstance(Object sourceBase) {
        return RegisterByInterface.findImplementation(ServiceProvider.class)
            .configureRequiredWhenByService(sourceBase);
    }
}