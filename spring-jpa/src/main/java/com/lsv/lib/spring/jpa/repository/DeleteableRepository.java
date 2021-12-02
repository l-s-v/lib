package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Deleteable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.concept.repository.Repository;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface DeleteableRepository<T extends Identifiable<ID>, ID extends Serializable, P extends Persistable<?>> extends Repository<T>, Deleteable<T> {

    default void delete(@NonNull T objIdentifiable) {
        jpaRepository().deleteById(objIdentifiable.id());
    }

    JpaRepository<P, ID> jpaRepository();
}