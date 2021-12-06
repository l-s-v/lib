package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;
import com.lsv.lib.core.helper.FactoryImplementation;

import java.io.Serializable;

public interface RepositoryImplementeable<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S extends Storable<P, ID>>
    extends
    Repository<I> {

    @SuppressWarnings("unchecked")
    default RepositoryProvider<I, ID, P, S> repositoryProvider() {
        return (RepositoryProvider<I, ID, P, S>) FactoryImplementation.createRepositoryProvider(this);
    }
}