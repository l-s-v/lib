package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.TypeOperation;
import com.lsv.lib.core.helper.HelperBeanValidation;

public interface DeletableService<T extends Identifiable<?>, R extends Deletable<T> & Repository<T>>
        extends ServiceWithRepository<T, R>, Deletable<T> {

    default void delete(T objIdentifiable) {
        this.validateDelete(objIdentifiable);
        this.repository().delete(objIdentifiable);
    }

    default void validateDelete(T objIdentifiable) {
        HelperBeanValidation.validate(this.validables(), objIdentifiable, TypeOperation.DELETE);
    }
}