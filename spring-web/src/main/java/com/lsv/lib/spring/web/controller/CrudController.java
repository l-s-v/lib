package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.*;
import com.lsv.lib.core.concept.service.Service;

import java.io.Serializable;

public interface CrudController<
        IN extends Identifiable<ID>,
        OUT extends Identifiable<ID>,
        S extends Service<OUT> & Creatable<OUT> & Readable<OUT> & Updatable<OUT> & Deletable<OUT>,
        ID extends Serializable>
        extends
        CreatableController<IN, OUT, S>,
        ReadableController<IN, OUT, S, ID>,
        UpdatableController<IN, OUT, S, ID>,
        DeletableController<IN, OUT, S, ID> {

    CrudControllerImpl<IN, OUT, S, ID> crudControllerImpl();

    @Override
    default CreateControllerImpl<IN, OUT, S> createControllerImpl() {
        return crudControllerImpl().createControllerImpl();
    }

    @Override
    default DeleteControllerImpl<IN, OUT, S, ID> deleUpdateControllerImpl() {
        return crudControllerImpl().deleteController();
    }

    @Override
    default ReadControllerImpl<IN, OUT, S, ID> readControllerImpl() {
        return crudControllerImpl().readControllerImpl();
    }

    @Override
    default UpdateControllerImpl<IN, OUT, S, ID> updateControllerImpl() {
        return crudControllerImpl().updateControllerImpl();
    }
}