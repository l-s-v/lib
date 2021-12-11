package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.pattern.register.RegisterByInterface;

import java.io.Serializable;
import java.util.NoSuchElementException;

public interface RepositoryProvider<
    I extends Identifiable<?>,
    ID extends Serializable,
    P extends Persistable<?>,
    S> {

    Mappable<I, P> mappable();

    S storable();

    RepositoryProvider<I, ID, P, S> configureRequiredWhenByService(Object sourceBase);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    static <
        I extends Identifiable<ID>,
        ID extends Serializable,
        P extends Persistable<ID>,
        S>
    RepositoryProvider<I, ID, P, S> findInstance(Object sourceBase) {
        try {
            return RegisterByInterface.findImplementation(RepositoryProvider.class)
                .configureRequiredWhenByService(sourceBase);
        } catch (NoSuchElementException e) {
            return RepositoryProviderBasicImpl.of(sourceBase);
        }
    }
}