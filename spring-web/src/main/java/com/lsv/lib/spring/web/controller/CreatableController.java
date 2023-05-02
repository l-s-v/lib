package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.service.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

public interface CreatableController<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT> & Creatable<OUT>> {

    CreateControllerImpl<IN, OUT, S> createControllerImpl();

    @PostMapping
    default ResponseEntity<Object> create(@RequestBody IN identifiable, UriComponentsBuilder uriBuilder) {
        return createControllerImpl().create(identifiable, uriBuilder);
    }
}