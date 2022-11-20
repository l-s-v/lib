package com.lsv.lib.spring.service;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.CreatableService;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface CreatableServiceTx<
        T extends Identifiable<?>,
        R extends Creatable<T> & Repository<T>>
        extends
        CreatableService<T, R> {

    @Transactional
    @Override
    default T create(@NonNull T identifiable) {
        return CreatableService.super.create(identifiable);
    }
}