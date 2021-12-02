package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Deleteable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.TypeOperation;
import com.lsv.lib.core.helper.HelperBeanValidation;

public interface DeleteableService<T extends Identifiable<?>, R extends Deleteable<T> & Repository<T>>
        extends ServiceWithRepository<T, R>, Deleteable<T> {

    default void delete(T objIdentifiable) {
        this.validateDelete(objIdentifiable);
        this.repository().delete(objIdentifiable);
    }

    default void validateDelete(T objIdentifiable) {
        HelperBeanValidation.validate(this.validables(), objIdentifiable, TypeOperation.DELETE);
    }
}