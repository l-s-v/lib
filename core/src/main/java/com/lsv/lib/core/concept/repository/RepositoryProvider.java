package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.behavior.Persistable;

import java.io.Serializable;

public interface RepositoryProvider<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S> {

    Mappable<I, P> mappable();

    RepositoryProvider<I, ID, P, S> mappable(Mappable<I, P> mappable);

    S storable();

    RepositoryProvider<I, ID, P, S> storable(S storable);

    RepositoryProvider<?, ?, ?, ?> setup(Object source);
}