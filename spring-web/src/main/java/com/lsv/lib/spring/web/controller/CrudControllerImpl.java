package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.*;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.helper.HelperClass;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Getter
@Setter
public class CrudControllerImpl<
        IN extends Identifiable<ID>,
        OUT extends Identifiable<ID>,
        S extends Service<OUT> & Creatable<OUT> & Readable<OUT> & Updatable<OUT> & Deletable<OUT>,
        ID extends Serializable>  {

    private final S service;
    private final Mappable<IN, OUT> mappable;
    private final String urlBase;

    private CreateControllerImpl<IN, OUT, S> createControllerImpl;
    private UpdateControllerImpl<IN, OUT, S, ID> updateControllerImpl;
    private ReadControllerImpl<IN, OUT, S, ID> readControllerImpl;
    private DeleteControllerImpl<IN, OUT, S, ID> deleteController;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public CrudControllerImpl(S service, Mappable<IN, OUT> mappable, CrudController<?, ?, ?, ?> root, String urlBase) {
        this.service = service;
        this.mappable = mappable;
        this.urlBase = urlBase;

        Class<?> classOut = HelperClass.identifyGenericsClass(root, 1);

        createControllerImpl(new CreateControllerImpl(service, mappable, urlBase));
        updateControllerImpl(new UpdateControllerImpl(service, mappable));
        readControllerImpl(new ReadControllerImpl(service, mappable, classOut));
        deleteController(new DeleteControllerImpl(service, classOut));
    }
}