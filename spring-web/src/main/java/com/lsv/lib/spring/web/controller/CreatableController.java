package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;
import lombok.NonNull;
import org.springframework.web.bind.annotation.PostMapping;

public interface CreatableController<
    IN extends Identifiable<?>,
    OUT extends Identifiable<?>,
    S extends Service<OUT> & Creatable<OUT>>
    extends
    Controller<IN, OUT, S> {

    @PostMapping
    default IN create(@NonNull IN identifiable) {
        return mappableOf(service().create(mappableTo(identifiable)));
    }
}