package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.Serializable;

public interface ReadableController<
        IN extends Identifiable<ID>,
        OUT extends Identifiable<ID>,
        S extends Service<OUT> & Readable<OUT>,
        ID extends Serializable> {

    int PAGEABLE_DEFAULT_PAGE = 0;
    int PAGEABLE_DEFAULT_SIZE = 10;
    String PAGEABLE_DEFAULT_SORT = "id";
    Sort.Direction PAGEABLE_DEFAULT_DIRECTION = Sort.Direction.DESC;

    Pageable PAGEABLE_DEFAULT = PageRequest.of(
            PAGEABLE_DEFAULT_PAGE,
            PAGEABLE_DEFAULT_SIZE,
            PAGEABLE_DEFAULT_DIRECTION,
            PAGEABLE_DEFAULT_SORT);

    ReadControllerImpl<IN, OUT, S, ID> readControllerImpl();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @GetMapping(Controller.PARAM_ID)
    default ResponseEntity<IN> findById(@PathVariable ID id) {
        return readControllerImpl().findById(id);
    }

    @GetMapping
    default ResponseEntity<Page<IN>> findByFilter(@PageableDefault(
            page = PAGEABLE_DEFAULT_PAGE,
            size = PAGEABLE_DEFAULT_SIZE,
            sort = PAGEABLE_DEFAULT_SORT,
            direction = Sort.Direction.DESC) Pageable pageable) {

        return readControllerImpl().findByFilter(pageable);
    }
}