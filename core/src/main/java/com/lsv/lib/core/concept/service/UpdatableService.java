package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.TypeOperation;
import com.lsv.lib.core.helper.HelperBeanValidation;

public interface UpdatableService<T extends Identifiable<?>, R extends Updatable<T> & Repository<T>>
        extends ServiceWithRepository<T, R>, Updatable<T> {

    default T update(T identifiable) {
        this.validateUpdate(identifiable);
        return this.repository().update(identifiable);
    }

    default void validateUpdate(T identifiable) {
        HelperBeanValidation.validate(identifiable);
        HelperBeanValidation.validate(this.validables(), identifiable, TypeOperation.UPDATE);
    }
}