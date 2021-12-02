package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Updateable;
import com.lsv.lib.core.concept.repository.Repository;
import lombok.NonNull;

import java.io.Serializable;

public interface UpdateableRepository<I extends Identifiable<ID>, ID extends Serializable, P extends Persistable<?>>
    extends Repository<I>, Updateable<I>, ProviderRepository<I, ID, P> {

    default I update(@NonNull I objIdentifiable) {
        return mapper().of(this.jpaRepository().save(mapper().to(objIdentifiable)));
    }
}