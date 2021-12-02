package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.TypeOperation;
import com.lsv.lib.core.helper.HelperBeanValidation;

public interface UpdatableService<T extends Identifiable<?>, R extends Updatable<T> & Repository<T>>
        extends ServiceWithRepository<T, R>, Updatable<T> {

    default T update(T objIdentifiable) {
        this.validateUpdate(objIdentifiable);
        return this.repository().update(objIdentifiable);
    }

    default void validateUpdate(T objIdentifiable) {
        HelperBeanValidation.validate(objIdentifiable);
        HelperBeanValidation.validate(this.validables(), objIdentifiable, TypeOperation.UPDATE);
    }
}