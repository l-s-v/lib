package com.lsv.lib.spring.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.UpdatableService;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface UpdatableServiceTx<
        T extends Identifiable<?>,
        R extends Updatable<T> & Repository<T>>
        extends
        UpdatableService<T, R> {

    @Transactional
    @Override
    default T update(@NonNull T identifiable) {
        return UpdatableService.super.update(identifiable);
    }
}