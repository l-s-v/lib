package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.TypeOperation;
import com.lsv.lib.core.helper.HelperBeanValidation;
import lombok.NonNull;

public interface UpdatableService<
    T extends Identifiable<?>,
    R extends Updatable<T> & Repository<T>>
    extends
    ServiceWithRepository<T, R>,
    Updatable<T> {

    default T update(@NonNull T identifiable) {
        validateUpdate(identifiable);
        return serviceProvider().repository().update(identifiable);
    }

    default void validateUpdate(@NonNull T identifiable) {
        HelperBeanValidation.validate(identifiable);
        HelperBeanValidation.validate(serviceProvider().validables(), identifiable, TypeOperation.UPDATE);
    }
}