package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;
import com.lsv.lib.core.concept.repository.RepositoryImplementeable;
import com.lsv.lib.core.concept.repository.RepositoryProvider;
import com.lsv.lib.spring.jpa.helper.RepositoryProviderSpringJpaImpl;

import java.io.Serializable;

public interface RepositoryImplementeableSpringJpa<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S extends Storable<P, ID>>
    extends
    RepositoryImplementeable<I, ID, P, S> {

    default RepositoryProvider<I, ID, P, S> repositoryProvider() {
        return new RepositoryProviderSpringJpaImpl<I, ID, P, S>().setup(this);
    }
}