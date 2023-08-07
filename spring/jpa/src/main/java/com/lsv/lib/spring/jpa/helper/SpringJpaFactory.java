package com.lsv.lib.spring.jpa.helper;

import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.spring.core.loader.SpringLoader;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

/**
 * @author Leandro da Silva Vieira
 */
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpringJpaFactory {

    private static EntityManager entityManager;
    private static RepositoryFactorySupport factory;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <T> T repository(Class<T> classe) {
        return factory().getRepository(classe);
    }

    public static <T extends Persistable<?>> SimpleJpaRepository<T, Serializable> simpleRepository(Class<T> classe) {
        return new SimpleJpaRepository<>(classe, entityManager());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static EntityManager entityManager() {
        if (entityManager == null) {
            entityManager = SpringLoader.bean(EntityManager.class);
        }
        return entityManager;
    }

    private static RepositoryFactorySupport factory() {
        if (factory == null) {
            factory = new JpaRepositoryFactory(entityManager());
        }
        return factory;
    }
}