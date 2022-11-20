package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.helper.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.Serializable;

public interface DeletableController<
        IN extends Identifiable<ID>,
        OUT extends Identifiable<ID>,
        ID extends Serializable,
        S extends Service<OUT> & Deletable<OUT>>
        extends
        Controller<IN, OUT, S> {

    @DeleteMapping(PARAM_ID)
    default ResponseEntity<Object> delete(@PathVariable ID id) {
        Log.of(this).debug("delete {}", id);

        service().delete((OUT) Identifiable.of(outClass(), id));
        return ResponseEntity.ok().build();
    }
}