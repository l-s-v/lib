package com.lsv.lib.spring.web.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.mapper.HelperMappable;
import com.lsv.lib.core.mapper.Mappable;
import com.lsv.lib.spring.web.controller.CrudController;

public interface TestControllerProvider<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S > {

    default Mappable<IN, OUT> mappable() {
        return (Mappable<IN, OUT>) HelperMappable.of(this, Identifiable.class, Identifiable.class);
    }

    S service();

    default String urlBaseWithId() {
        return urlBase() + CrudController.PARAM_ID;
    }

    ObjectMapper objectMapper();

    String urlBase();

    IN newObjectIn();

    OUT expectedObjectOut();

    IN newObjectInInvalidForCreateAndUpdate();
}