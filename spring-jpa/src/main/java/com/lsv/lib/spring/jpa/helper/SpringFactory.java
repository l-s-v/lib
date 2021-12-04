package com.lsv.lib.spring.jpa.helper;

import com.lsv.lib.core.behavior.Persistable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.io.Serializable;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public final class SpringFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static EntityManager entityManager;
    private static RepositoryFactorySupport factory;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        synchronized (this) {
            if (SpringFactory.applicationContext == null) {
                SpringFactory.applicationContext = applicationContext;
                entityManager = bean(EntityManager.class);
                factory = new JpaRepositoryFactory(entityManager);
            }
        }
    }

    public static <T> T bean(Class<T> classe) {
        return applicationContext.getBean(classe);
    }

    public static <T> T repository(Class<T> classe) {
        return factory.getRepository(classe);
    }

    public static <T extends Persistable<?>> SimpleJpaRepository<T, Serializable> simpleRepository(Class<T> classe) {
        return new SimpleJpaRepository<>(classe, entityManager);
    }
}