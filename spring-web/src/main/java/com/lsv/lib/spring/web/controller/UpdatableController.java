package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.helper.HelperBeanValidation;
import com.lsv.lib.core.helper.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;

public interface UpdatableController<
        ID extends Serializable,
        IN extends Identifiable<ID>,
        OUT extends Identifiable<ID>,
        S extends Service<OUT> & Updatable<OUT>>
        extends
        Controller<IN, OUT, S> {

    @PutMapping(PARAM_ID)
    default ResponseEntity<IN> update(@PathVariable ID id, @RequestBody IN identifiable) {
        Log.of(this).debug("update {} {}", id, identifiable);

        HelperBeanValidation.validate(identifiable); // @Valid didn't work
        identifiable.setId(id);
        mappableOf(service().update(mappableTo(identifiable)));
        return ResponseEntity.ok().build();
    }
}