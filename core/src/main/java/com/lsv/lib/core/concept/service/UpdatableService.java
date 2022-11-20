package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.TypeOperation;
import com.lsv.lib.core.helper.HelperBeanValidation;
import com.lsv.lib.core.helper.Log;
import lombok.NonNull;

public interface UpdatableService<
        T extends Identifiable<?>,
        R extends Updatable<T> & Repository<T>>
        extends
        ServiceWithRepository<T, R>,
        Updatable<T> {

    default T update(@NonNull T identifiable) {
        Log.of(this).debug("update {}", identifiable);

        validateUpdate(identifiable);
        return repository().update(identifiable);
    }

    default void validateUpdate(@NonNull T identifiable) {
        HelperBeanValidation.validate(identifiable);
        HelperBeanValidation.validate(validables(), identifiable, TypeOperation.UPDATE);
    }
}