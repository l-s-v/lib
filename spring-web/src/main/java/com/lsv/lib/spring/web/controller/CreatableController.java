package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.helper.HelperBeanValidation;
import com.lsv.lib.core.helper.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

public interface CreatableController<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT> & Creatable<OUT>>
        extends
        Controller<IN, OUT, S> {

    @PostMapping
    default ResponseEntity<Object> create(@RequestBody IN identifiable, UriComponentsBuilder uriBuilder) {
        Log.of(this).debug("create {}", identifiable);

        HelperBeanValidation.validate(identifiable); // @Valid didn't work
        OUT objOut = (OUT) mappableOf(service().create(mappableTo(identifiable)));

        return ResponseEntity.created(
                uriBuilder
                        .path(urlBase())
                        .pathSegment(objOut.getId().toString())
                        .build().toUri())
                .build();
    }
}