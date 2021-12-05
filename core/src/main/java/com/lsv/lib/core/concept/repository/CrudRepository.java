package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;

import java.io.Serializable;

public interface CrudRepository<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S extends Storable<P, ID>>
    extends
    CreatableRepository<I, ID, P, S>,
    UpdatableRepository<I, ID, P, S>,
    DeletableRepository<I, ID, P, S>,
    ReadableRepository<I, ID, P, S> {
}