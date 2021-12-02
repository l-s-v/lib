package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Deleteable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.concept.repository.Repository;
import lombok.NonNull;

import java.io.Serializable;

public interface DeleteableRepository<I extends Identifiable<ID>, ID extends Serializable, P extends Persistable<?>>
    extends Repository<I>, Deleteable<I>, ProviderRepository<I, ID, P> {

    default void delete(@NonNull I objIdentifiable) {
        jpaRepository().deleteById(objIdentifiable.getId());
    }
}