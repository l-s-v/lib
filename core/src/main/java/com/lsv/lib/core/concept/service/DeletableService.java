package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.TypeOperation;
import com.lsv.lib.core.helper.HelperBeanValidation;
import lombok.NonNull;

public interface DeletableService<
    T extends Identifiable<?>,
    R extends Deletable<T> & Repository<T>>
    extends
    ServiceWithRepository<T, R>,
    Deletable<T> {

    @Override
    default void delete(@NonNull T identifiable) {
        validateDelete(identifiable);
        repository().delete(identifiable);
    }

    default void validateDelete(@NonNull T identifiable) {
        HelperBeanValidation.validate(validables(), identifiable, TypeOperation.DELETE);
    }
}