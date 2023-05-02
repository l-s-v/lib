package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.helper.HelperBeanValidation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class CreateControllerImpl<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT> & Creatable<OUT>>
        implements
        Controller<IN, OUT, S> {

    private final S service;
    private final Mappable<IN, OUT> mappable;
    private final String urlBase;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public ResponseEntity<Object> create(IN identifiable, UriComponentsBuilder uriBuilder) {
        log.debug("create {}", identifiable);

        HelperBeanValidation.validate(identifiable); // @Valid didn't work
        OUT objOut;

        if (mappable == null){
            objOut = service().create((OUT) identifiable);
        } else {
            objOut = (OUT) mappableOf(service().create(mappableTo(identifiable)));
        }

        return ResponseEntity.created(
                        uriBuilder
                                .path(urlBase())
                                .pathSegment(objOut.getId().toString())
                                .build().toUri())
                .build();
    }
}