package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.TypeOperation;
import com.lsv.lib.core.helper.HelperBeanValidation;

import java.util.Optional;

public interface ReadableService<T extends Identifiable<?>, R extends Readable<T> & Repository<T>>
        extends ServiceWithRepository<T, R>, Readable<T> {

    default Optional<T> findById(T objIdentifiable) {
        this.validateFindById(objIdentifiable);
        return this.repository().findById(objIdentifiable);
    }

    default ListDto<T> findByFilter(Filter<T> filter) {
        return this.repository().findByFilter(filter);
    }

    default void validateFindById(T objIdentifiable) {
        HelperBeanValidation.validate(this.validables(), objIdentifiable, TypeOperation.READ);
    }
}