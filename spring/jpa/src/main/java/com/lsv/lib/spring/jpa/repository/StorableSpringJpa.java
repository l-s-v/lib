package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;
import com.lsv.lib.core.loader.Loader;
import com.lsv.lib.spring.jpa.helper.SpringJpaFactory;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * Uses Spring Jpa(JpaRepository, JpaSpecificationExecutor) to provide the storage part.
 *
 * @author Leandro da Silva Vieira
 */
public interface StorableSpringJpa<
        P extends Persistable<ID>,
        ID extends Serializable>
        extends
        Storable<P, ID>,
        JpaRepository<P, ID>,
        JpaSpecificationExecutor<P> {

    /**
     * Spring JPA doesn't provide merge method, so it uses directly from hibernate's Session bean.
     */
    @Override
    default <S extends P> S merge(S entity) {
        return (S) Loader.of(Session.class).findUniqueImplementationByFirstLoader().merge(entity);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Looks for some existing bean for the repository interface.
     * If not found, it creates an instance of SimpleJpaRepository for the object of type Persistable.
     */
    @SuppressWarnings("unchecked")
    static <S> S of(Class<?> classTypeStorable, Class<? extends Persistable> classTypePersistable) {
        try {
            return (S) SpringJpaFactory.repository(classTypeStorable);
        } catch (NoSuchElementException e) {
            return (S) SpringJpaFactory.simpleRepository(classTypePersistable);
        }
    }
}