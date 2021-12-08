package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.pattern.register.RegisterByInterface;

import java.io.Serializable;

public interface RepositoryProvider<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S> {

    Mappable<I, P> mappable();

    S storable();

    RepositoryProvider<?, ?, ?, ?> setupRequiredWhenByService(Object sourceBased);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    static <
        I extends Identifiable<ID>,
        ID extends Serializable,
        P extends Persistable<ID>,
        S>
    RepositoryProvider<I, ID, P, S> findInstance(Object sourceBased) {
        return RegisterByInterface.findImplementation(RepositoryProvider.class)
            .setupRequiredWhenByService(sourceBased);
    }
}