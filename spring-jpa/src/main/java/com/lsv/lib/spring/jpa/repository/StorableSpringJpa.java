package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.loader.Loader;
import com.lsv.lib.spring.jpa.helper.SpringJpaFactory;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;
import java.util.NoSuchElementException;

public interface StorableSpringJpa<
        P extends Persistable<ID>,
        ID extends Serializable>
        extends
        Storable<P, ID>,
        JpaRepository<P, ID>,
        JpaSpecificationExecutor<P> {

    @Override
    default <S extends P> S merge(S entity) {
        return (S) Loader.findImplementation(Session.class).merge(entity);
    }

    @SuppressWarnings("unchecked")
    static <S> S findInstance(Object sourceBase) {
        try {
            Class<?> classType = HelperClass.identifyGenericsClass(sourceBase, StorableSpringJpa.class);
            return (S) SpringJpaFactory.repository(HelperClass.identifyGenericsClass(sourceBase, classType));
        } catch (NoSuchElementException e) {
            return (S) SpringJpaFactory.simpleRepository(HelperClass.identifyGenericsClass(sourceBase, Persistable.class));
        }
    }
}