package com.lsv.lib.spring.web.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;
import org.springframework.test.web.servlet.MockMvc;

public interface TestControllerProvider<
    IN extends Identifiable<?>,
    OUT extends Identifiable<?>,
    S extends Service<OUT>> {

    MockMvc mockMvc();

    ObjectMapper objectMapper();

    Mappable<IN, OUT> mappable();

    S serviceMock();

    String urlBase();

    default String urlBaseWithId() {
        return urlBase() + Controller.PARAM_ID;
    }

    IN newObjectIn();

    OUT expectedObjectOut();
}