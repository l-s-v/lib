package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.*;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.concept.service.validations.TypeOperation;
import com.lsv.lib.core.concept.service.validations.Validable;
import com.lsv.lib.core.concept.service.validations.ValidableIdentifiable;
import com.lsv.lib.core.helper.HelperBeanValidation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Provides standard CRUD methods for Service.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CrudService {

    public static final Validable<?> VALIDABLE_DEFAULT = new ValidableIdentifiable();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            IN extends Identifiable<?>,
            R extends Creatable<IN>>
    IN create(R repository,
              Validable<IN> validable,
              @NonNull IN identifiable) {

        log.debug("create {}", identifiable);

        HelperBeanValidation.validate(identifiable);
        selValidables(validable).validate(identifiable, TypeOperation.CREATE);

        return repository.create(identifiable);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            IN extends Identifiable<?>,
            R extends Updatable<IN>>
    IN update(R repository,
              Validable<IN> validable,
              @NonNull IN identifiable) {

        log.debug("update {}", identifiable);

        HelperBeanValidation.validate(identifiable);
        selValidables(validable).validate(identifiable, TypeOperation.UPDATE);

        return repository.update(identifiable);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            IN extends Identifiable<?>,
            R extends Deletable<IN>>
    void delete(R repository,
                Validable<IN> validable,
                @NonNull IN identifiable) {

        log.debug("delete {}", identifiable);

        selValidables(validable).validate(identifiable, TypeOperation.DELETE);

        repository.delete(identifiable);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            IN extends Identifiable<?>,
            R extends Readable<IN>>
    Optional<IN> findById(R repository,
                          Validable<IN> validable,
                          @NonNull IN identifiable) {

        log.debug("findById {}", identifiable);

        selValidables(validable).validate(identifiable, TypeOperation.READ);

        return repository.findById(identifiable);
    }

    public static <
            IN extends Identifiable<?>,
            R extends Readable<IN>>
    ListDto<IN> findByFilter(R repository,
                             @NonNull Filter<IN> filter) {

        log.debug("findByFilter {}", filter);

        return repository.findByFilter(filter);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static <IN extends Identifiable<?>> Validable<IN> selValidables(Validable<IN> validable) {
        return validable != null ? validable : (Validable<IN>) (Object) VALIDABLE_DEFAULT;
    }
}