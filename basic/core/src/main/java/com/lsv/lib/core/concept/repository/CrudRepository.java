package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.mapper.Mappable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;
import com.lsv.lib.core.exception.DisplayException;
import com.lsv.lib.core.exception.helper.ProblemDetail;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;

import static com.lsv.lib.core.exception.message.MessageDisplayExceptionEnum.ID_NOT_FOUND;

/**
 * Provides standard CRUD methods for Repository.
 *
 * @author leandro.vieira
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CrudRepository {

    public static final DisplayException EXCEPTION_ID_NOT_FOUND = ID_NOT_FOUND.displayException(ProblemDetail.NOT_FOUND);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            P extends Persistable<ID>,
            S extends Storable<P, ID>>
    IN create(S storable,
              Mappable<IN, P> mappable,
            @NonNull IN identifiable) {

        log.debug("create {}", identifiable);

        var persistable = mappable.to(identifiable);
        persistable = storable.isUtilizeSave()
                ? storable.save(persistable)
                : storable.merge(persistable);

        return mappable.of(persistable);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            P extends Persistable<ID>,
            S extends Storable<P, ID>>
    IN update(S storable,
              Mappable<IN, P> mappable,
              @NonNull IN identifiable) {

        log.debug("update {}", identifiable);

        if(! storable.existsById(identifiable.getId())) {
            throw EXCEPTION_ID_NOT_FOUND;
        }

        return mappable.of(
                storable.save(
                        mappable.to(identifiable)));
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            P extends Persistable<ID>,
            S extends Storable<P, ID>>
    void delete(S storable,
                @NonNull IN identifiable) {

        log.debug("delete {}", identifiable);

        if(! storable.existsById(identifiable.getId())) {
            throw EXCEPTION_ID_NOT_FOUND;
        }

        storable.deleteById(identifiable.getId());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            P extends Persistable<ID>,
            S extends Storable<P, ID>>
    Optional<IN> findById(S storable,
                          Mappable<IN, P> mappable,
                          Consumer<P> initializeLazyFindById,
                          @NonNull IN identifiable) {

        log.debug("findById {}", identifiable);

        return storable.findById(identifiable.getId())
                .map(p -> {
                    if(initializeLazyFindById != null) {
                        initializeLazyFindById.accept(p);
                    }
                    return mappable.of(p);
                });
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
}