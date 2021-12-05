package com.lsv.lib.spring.jpa.helper;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.helper.RepositoryProviderImpl;
import com.lsv.lib.spring.jpa.repository.StorableSpringJpa;

import java.io.Serializable;
import java.util.NoSuchElementException;

public class RepositoryProviderSpringJpaImpl<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S>
    extends RepositoryProviderImpl<I, ID, P, S> {

    @SuppressWarnings("unchecked")
    @Override
    protected S findStorable() {
        try {
            Class<?> classType = HelperClass.identifyGenericsClass(sourceToFindGenerics(), StorableSpringJpa.class);
            return (S) SpringFactory.repository(HelperClass.identifyGenericsClass(sourceToFindGenerics(), classType));
        } catch (NoSuchElementException e) {
            return (S) SpringFactory.simpleRepository(HelperClass.identifyGenericsClass(sourceToFindGenerics(), Persistable.class));
        }
    }
}