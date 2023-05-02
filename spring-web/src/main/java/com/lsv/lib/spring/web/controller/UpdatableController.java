package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;

public interface UpdatableController<
        IN extends Identifiable<ID>,
        OUT extends Identifiable<ID>,
        S extends Service<OUT> & Updatable<OUT>,
        ID extends Serializable> {

    UpdateControllerImpl<IN, OUT, S, ID> updateControllerImpl();

    @PutMapping(Controller.PARAM_ID)
    default ResponseEntity<IN> update(@PathVariable ID id, @RequestBody IN identifiable) {
        return updateControllerImpl().update(id, identifiable);
    }
}