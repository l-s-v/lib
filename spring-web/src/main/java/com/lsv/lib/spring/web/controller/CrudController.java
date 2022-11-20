package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.*;
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
        UpdatableController<ID, IN, OUT, S>,
        DeletableController<IN, OUT, ID, S> {
}