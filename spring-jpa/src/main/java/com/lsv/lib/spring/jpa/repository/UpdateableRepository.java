package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mapper;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Updateable;
import com.lsv.lib.core.concept.repository.Repository;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UpdateableRepository<T extends Identifiable<ID>, ID extends Serializable, P extends Persistable<?>> extends Repository<T>, Updateable<T> {

    default T update(@NonNull T objIdentifiable) {
        return mapper().of(this.jpaRepository().save(mapper().to(objIdentifiable)));
    }

    Mapper<T, P> mapper();
    JpaRepository<P, ID> jpaRepository();
}