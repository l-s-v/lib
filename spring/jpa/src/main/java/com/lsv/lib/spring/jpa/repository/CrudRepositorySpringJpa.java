package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.mapper.Mappable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.concept.repository.CrudRepository;
import com.lsv.lib.spring.core.converter.ConverterSpringJpa;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Provides standard CRUD methods for Repository with Spring and JPA.
 *
 * @author leandro.vieira
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CrudRepositorySpringJpa {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            P extends Persistable<ID>,
            S extends Storable<P, ID>>
    IN create(S storable,
              Mappable<IN, P> mappable,
              IN identifiable) {
        return CrudRepository.create(storable, mappable, identifiable);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            P extends Persistable<ID>,
            S extends Storable<P, ID>>
    IN update(S storable,
              Mappable<IN, P> mappable,
              IN identifiable) {

        return CrudRepository.update(storable, mappable, identifiable);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            P extends Persistable<ID>,
            S extends Storable<P, ID>>
    void delete(S storable,
                IN identifiable) {

        CrudRepository.delete(storable, identifiable);
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
                          IN identifiable) {

        return CrudRepository.findById(storable, mappable, initializeLazyFindById, identifiable);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            P extends Persistable<ID>,
            S extends StorableSpringJpa<P, ID>>
    ListDto<IN> findByFilter(S storable,
                             Mappable<IN, P> mappable,
                             Specification<P> specification,
                             @NonNull Filter<IN> filter) {

        log.debug("findByFilter {}", filter);

        Pageable pageable = ConverterSpringJpa.to(filter);
        Example<P> example = null;

        if (specification == null && filter.obj() != null) {
            example = Example.of(mappable.to(filter.obj()));
        }

        if (pageable != null) {
            return findAllPageable(storable, mappable, example, specification, pageable);
        } else {
            return findAllSorted(storable, mappable, example, specification, ConverterSpringJpa.to(filter.orderBies()));
        }
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            P extends Persistable<ID>,
            S extends StorableSpringJpa<P, ID>>
    ListDto<IN> findAllPageable(S storable,
                                Mappable<IN, P> mappable,
                                Example<P> example,
                                Specification<P> specification,
                                @NonNull Pageable pageable) {

        Page<P> page;

        if (specification != null) {
            page = storable.findAll(specification, pageable);
        } else if (example != null) {
            page = storable.findAll(example, pageable);
        } else {
            page = storable.findAll(pageable);
        }

        return Optional.of(page)
                .map(ps -> ConverterSpringJpa.of(page, mappable))
                .orElse(ListDto.empty());
    }

    private static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            P extends Persistable<ID>,
            S extends StorableSpringJpa<P, ID>>
    ListDto<IN> findAllSorted(S storable,
                              Mappable<IN, P> mappable,
                              Example<P> example,
                              Specification<P> specification,
                              @NonNull Sort sort) {

        List<P> results;

        if (specification != null) {
            results = storable.findAll(specification, sort);
        } else if (example != null) {
            results = storable.findAll(example, sort);
        } else {
            results = storable.findAll(sort);
        }

        return Optional.of(results)
                .map(ps -> ListDto
                        .of(mappable.of(ps))
                        .get())
                .orElse(ListDto.empty());
    }
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
}