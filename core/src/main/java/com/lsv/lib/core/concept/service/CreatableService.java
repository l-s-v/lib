package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.TypeOperation;
import com.lsv.lib.core.helper.HelperBeanValidation;

public interface CreatableService<T extends Identifiable<?>, R extends Creatable<T> & Repository<T>>
    extends ServiceWithRepository<T, R>, Creatable<T> {

    default T create(T identifiable) {
        this.validateCreate(identifiable);
        return this.repository().create(identifiable);
    }

    default void validateCreate(T identifiable) {
        HelperBeanValidation.validate(identifiable);
        HelperBeanValidation.validate(this.validables(), identifiable, TypeOperation.CREATE);
    }
}