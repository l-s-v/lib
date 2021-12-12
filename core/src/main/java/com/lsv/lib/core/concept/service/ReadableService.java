package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.TypeOperation;
import com.lsv.lib.core.helper.HelperBeanValidation;
import lombok.NonNull;

import java.util.Optional;

public interface ReadableService<
    T extends Identifiable<?>,
    R extends Readable<T> & Repository<T>>
    extends
    ServiceWithRepository<T, R>,
    Readable<T> {

    default Optional<T> findById(@NonNull T identifiable) {
        validateFindById(identifiable);
        return repository().findById(identifiable);
    }

    default ListDto<T> findByFilter(@NonNull Filter<T> filter) {
        return repository().findByFilter(filter);
    }

    default void validateFindById(@NonNull T identifiable) {
        HelperBeanValidation.validate(validables(), identifiable, TypeOperation.READ);
    }
}