package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.concept.repository.CrudRepository;
import com.lsv.lib.core.concept.repository.RepositoryProvider;

import java.io.Serializable;

public interface CrudRepositorySpringJpa<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S extends StorableSpringJpa<P, ID>>
    extends
    CrudRepository<I, ID, P, S>,
    ReadableRepositorySpringJpa<I, ID, P, S> {

    static <
        I extends Identifiable<ID>,
        ID extends Serializable,
        P extends Persistable<ID>,
        S extends StorableSpringJpa<P, ID>>
    CrudRepository<I, ID, P, S> of(Object sourceBase) {
        return of(RepositoryProvider.findInstance(sourceBase));
    }

    static <
        I extends Identifiable<ID>,
        ID extends Serializable,
        P extends Persistable<ID>,
        S extends StorableSpringJpa<P, ID>>
    CrudRepository<I, ID, P, S> of(RepositoryProvider<I, ID, P, S> repositoryProvider) {
        return (CrudRepositorySpringJpa<I, ID, P, S>) () -> repositoryProvider;
    }
}