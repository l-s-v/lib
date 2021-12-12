package com.lsv.lib.core.concept.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.pattern.register.RegisterByInterface;

import java.util.NoSuchElementException;

public interface ControllerProvider<
    IN extends Identifiable<?>,
    OUT extends Identifiable<?>,
    S extends Service<OUT>> {

    Mappable<IN, OUT> mappable();

    S service();

    ControllerProvider<IN, OUT, S> configureRequiredWhenByService(ControllerProvider<IN, OUT, S> sourceBase);

    OUT mappableTo(IN identifiable);

    IN mappableOf(OUT identifiable);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    static <
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT>>
    ControllerProvider<IN, OUT, S> findInstance(ControllerProvider<IN, OUT, S> sourceBase) {
        try {
            return RegisterByInterface.findImplementation(ControllerProvider.class)
                .configureRequiredWhenByService(sourceBase);
        } catch (NoSuchElementException e) {
            return ControllerProviderBasicImpl.of(sourceBase);
        }
    }
}