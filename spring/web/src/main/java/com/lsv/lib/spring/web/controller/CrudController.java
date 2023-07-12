package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.*;
import com.lsv.lib.core.concept.repository.CrudRepository;
import com.lsv.lib.core.helper.HelperBeanValidation;
import com.lsv.lib.spring.core.converter.ConverterSpringJpa;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;

/**
 * Provides standard CRUD methods for Controller.
 *
 * @author leandro.vieira
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CrudController {

    public static final String PARAM_ID = "/{id}";

    public static final int PAGEABLE_DEFAULT_PAGE = 0;
    public static final int PAGEABLE_DEFAULT_SIZE = 10;
    public static final String PAGEABLE_DEFAULT_SORT = "id";
    public static final Sort.Direction PAGEABLE_DEFAULT_DIRECTION = Sort.Direction.DESC;

    public static final Pageable PAGEABLE_DEFAULT = PageRequest.of(
            PAGEABLE_DEFAULT_PAGE,
            PAGEABLE_DEFAULT_SIZE,
            PAGEABLE_DEFAULT_DIRECTION,
            PAGEABLE_DEFAULT_SORT);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            S extends Creatable<IN>>
    ResponseEntity<Object> create(
            S service,
            String urlBase,
            IN identifiable, UriComponentsBuilder uriBuilder) {

        log.debug("create {}", identifiable);

        HelperBeanValidation.validate(identifiable);
        IN objOut = service.create(identifiable);

        return ResponseEntity.created(
                        uriBuilder
                                .path(urlBase)
                                .pathSegment(objOut.getId().toString())
                                .build().toUri())
                .build();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            S extends Updatable<IN>>
    ResponseEntity<IN> update(
            S service,
            ID id, IN identifiable) {

        log.debug("update {} {}", id, identifiable);

        HelperBeanValidation.validate(identifiable);
        identifiable.setId(id);
        service.update(identifiable);

        return ResponseEntity.ok().build();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            S extends Deletable<IN>>
    ResponseEntity<Object> delete(
            S service,
            Class<IN> inClass,
            ID id) {
        log.debug("delete {}", id);

        service.delete(Identifiable.of(inClass, id));
        return ResponseEntity.ok().build();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            S extends Readable<IN>>
    ResponseEntity<IN> findById(
            S service,
            Class<IN> inClass,
            ID id) {

        log.debug("findById {}", id);

        return ResponseEntity.ok(
                service.findById(Identifiable.of(inClass, id)).orElseThrow(() -> CrudRepository.EXCEPTION_ID_NOT_FOUND));
    }

    public static <
            ID extends Serializable,
            IN extends Identifiable<ID>,
            S extends Readable<IN>>
    ResponseEntity<Page<IN>> findByFilter(
            S service,
            Pageable pageable) {

        log.debug("findByFilter {}", pageable);

        return ResponseEntity.ok(ConverterSpringJpa.to(
                service.findByFilter(ConverterSpringJpa.of(pageable)),
                pageable));
    }
}