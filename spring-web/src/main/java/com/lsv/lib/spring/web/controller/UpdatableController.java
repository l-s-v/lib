package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.helper.HelperBeanValidation;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UpdatableController<
    IN extends Identifiable<?>,
    OUT extends Identifiable<?>,
    S extends Service<OUT> & Updatable<OUT>>
    extends
    Controller<IN, OUT, S> {

    @PutMapping
    default IN update(@RequestBody IN identifiable) {
        HelperBeanValidation.validate(identifiable); // @Valid didn't work
        return mappableOf(service().update(mappableTo(identifiable)));
    }
}