package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mapper;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.spring.jpa.helper.ProviderRepositoryImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface ProviderRepository<I extends Identifiable<ID>, ID extends Serializable, P extends Persistable<?>> {

    default Mapper<I, P> mapper() {
        return ProviderRepositoryImpl.mapper(this);
    }

    default ProviderRepository<I, ID, P> mapper(Mapper<I, P> mapper) {
        ProviderRepositoryImpl.mapper(this, mapper);
        return this;
    }

    default JpaRepository<P, ID> jpaRepository() {
        return ProviderRepositoryImpl.jpaRepository(this);
    }

    default ProviderRepository<I, ID, P> jpaRepository(JpaRepository<P, ID> jpaRepository) {
        ProviderRepositoryImpl.jpaRepository(this, jpaRepository);
        return this;
    }
}