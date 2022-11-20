package com.lsv.lib.spring.web.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;

public interface TestControllerProvider<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT>> {

    default Mappable<IN, OUT> mappable() {
        return (Mappable<IN, OUT>) Mappable.findInstance(this, Identifiable.class, Identifiable.class);
    }

    default S service() {
        return (S) Service.findInstance(this, Service.class);
    }

    default String urlBaseWithId() {
        return urlBase() + Controller.PARAM_ID;
    }

    ObjectMapper objectMapper();

    String urlBase();

    IN newObjectIn();

    OUT expectedObjectOut();

    IN newObjectInInvalidForCreateAndUpdate();
}