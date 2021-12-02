package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.TypeOperation;
import com.lsv.lib.core.helper.HelperBeanValidation;

public interface CreatableService<T extends Identifiable<?>, R extends Creatable<T> & Repository<T>>
    extends ServiceWithRepository<T, R>, Creatable<T> {

    default T create(T objIdentifiable) {
        this.validateCreate(objIdentifiable);
        return this.repository().create(objIdentifiable);
    }

    default void validateCreate(T objIdentifiable) {
        HelperBeanValidation.validate(objIdentifiable);
        HelperBeanValidation.validate(this.validables(), objIdentifiable, TypeOperation.CREATE);
    }
}