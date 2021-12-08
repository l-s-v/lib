package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.TypeOperation;
import com.lsv.lib.core.helper.HelperBeanValidation;
import lombok.NonNull;

public interface CreatableService<
    T extends Identifiable<?>,
    R extends Creatable<T> & Repository<T>>
    extends
    ServiceWithRepository<T, R>,
    Creatable<T> {

    @Override
    default T create(@NonNull T identifiable) {
        validateCreate(identifiable);
        return serviceProvider().repository().create(identifiable);
    }

    default void validateCreate(@NonNull T identifiable) {
        HelperBeanValidation.validate(identifiable);
        HelperBeanValidation.validate(serviceProvider().validables(), identifiable, TypeOperation.CREATE);
    }
}