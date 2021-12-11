package com.lsv.lib.spring.jpa.helper;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.concept.repository.RepositoryProviderBasicImpl;
import com.lsv.lib.spring.jpa.repository.StorableSpringJpa;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class RepositoryProviderSpringJpaImpl<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S>
    extends
    RepositoryProviderBasicImpl<I, ID, P, S> {

    public static <
        I extends Identifiable<ID>,
        ID extends Serializable,
        P extends Persistable<ID>,
        S>
    RepositoryProviderBasicImpl<I, ID, P, S> of(Object sourceBase) {
        return (RepositoryProviderBasicImpl<I, ID, P, S>) new RepositoryProviderSpringJpaImpl<>().configureRequiredWhenByService(sourceBase);
    }

    @Override
    protected S findStorable(Object sourceBase) {
        return StorableSpringJpa.findInstance(sourceBase);
    }
}