package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.helper.HelperBeanValidation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class UpdateControllerImpl<
        IN extends Identifiable<ID>,
        OUT extends Identifiable<ID>,
        S extends Service<OUT> & Updatable<OUT>,
        ID extends Serializable>
        implements
        Controller<IN, OUT, S> {

    private final S service;
    private final Mappable<IN, OUT> mappable;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public ResponseEntity<IN> update(ID id, IN identifiable) {
        log.debug("update {} {}", id, identifiable);

        HelperBeanValidation.validate(identifiable); // @Valid didn't work
        identifiable.setId(id);

        if (mappable == null){
            service().update((OUT) identifiable);
        } else {
            mappableOf(service().update(mappableTo(identifiable)));
        }

        return ResponseEntity.ok().build();
    }
}