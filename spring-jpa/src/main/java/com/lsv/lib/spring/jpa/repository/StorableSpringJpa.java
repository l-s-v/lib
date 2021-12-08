package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.spring.jpa.helper.SpringFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.NoSuchElementException;

@Repository
public interface StorableSpringJpa
    <
        P extends Persistable<ID>,
        ID extends Serializable>
    extends
    Storable<P, ID>,
    JpaRepository<P, ID>,
    JpaSpecificationExecutor<P> {

    @SuppressWarnings("unchecked")
    static <S> S findInstance(Object sourceBased) {
        try {
            Class<?> classType = HelperClass.identifyGenericsClass(sourceBased, StorableSpringJpa.class);
            return (S) SpringFactory.repository(HelperClass.identifyGenericsClass(sourceBased, classType));
        } catch (NoSuchElementException e) {
            return (S) SpringFactory.simpleRepository(HelperClass.identifyGenericsClass(sourceBased, Persistable.class));
        }
    }
}