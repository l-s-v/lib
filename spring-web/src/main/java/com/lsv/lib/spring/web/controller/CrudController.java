package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.*;
import com.lsv.lib.core.concept.controller.ControllerProvider;
import com.lsv.lib.core.concept.service.Service;

import java.io.Serializable;

public interface CrudController<
    IN extends Identifiable<ID>,
    OUT extends Identifiable<ID>,
    ID extends Serializable,
    S extends Service<OUT> & Creatable<OUT> & Readable<OUT> & Updatable<OUT> & Deletable<OUT>>
    extends
    CreatableController<IN, OUT, S>,
    ReadableController<IN, OUT, ID, S>,
    UpdatableController<IN, OUT, S>,
    DeletableController<IN, OUT, ID, S> {

    static <
        IN extends Identifiable<ID>,
        OUT extends Identifiable<ID>,
        ID extends Serializable,
        S extends Service<OUT> & Creatable<OUT> & Readable<OUT> & Updatable<OUT> & Deletable<OUT>>
    CrudController<IN, OUT, ID, S> ofProvider(ControllerProvider<IN, OUT, S> controllerProvider) {
        return () -> controllerProvider;
    }
}