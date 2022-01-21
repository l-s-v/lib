package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;
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
    default void delete(@PathVariable ID id) {
        service().delete((OUT) Identifiable.of(outClass(), id));
    }
}