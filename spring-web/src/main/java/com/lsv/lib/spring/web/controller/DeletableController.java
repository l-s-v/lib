package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.Serializable;

public interface DeletableController<
        IN extends Identifiable<ID>,
        OUT extends Identifiable<ID>,
        S extends Service<OUT> & Deletable<OUT>,
        ID extends Serializable> {

    DeleteControllerImpl<IN, OUT, S, ID> deleUpdateControllerImpl();

    @DeleteMapping(Controller.PARAM_ID)
    default ResponseEntity<Object> delete(@PathVariable ID id) {
        return deleUpdateControllerImpl().delete(id);
    }
}