package com.lsv.lib.spring.service;

import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.DeletableService;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface DeletableServiceTx<
        T extends Identifiable<?>,
        R extends Deletable<T> & Repository<T>>
        extends
        DeletableService<T, R> {

    @Transactional
    @Override
    default void delete(@NonNull T identifiable) {
        DeletableService.super.delete(identifiable);
    }
}