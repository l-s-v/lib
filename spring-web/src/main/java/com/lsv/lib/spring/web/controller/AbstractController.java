package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;

public abstract class AbstractController<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT>>
        implements
        Controller<IN, OUT, S> {
}