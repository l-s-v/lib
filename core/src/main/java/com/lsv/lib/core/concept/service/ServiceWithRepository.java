package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.service.validations.Validable;
import com.lsv.lib.core.concept.service.validations.ValidableIdentifiable;

import java.util.List;

public interface ServiceWithRepository<T extends Identifiable<?>, R> extends Service<T> {

    R repository();

    @SuppressWarnings({"unchecked"})
    default List<Validable<T>> validables() {
        return List.of((Validable<T>) ValidableIdentifiable.of().get());
    }
}