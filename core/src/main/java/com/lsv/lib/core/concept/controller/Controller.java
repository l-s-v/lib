package com.lsv.lib.core.concept.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.helper.HelperClass;

public interface Controller<
    IN extends Identifiable<?>,
    OUT extends Identifiable<?>,
    S extends Service<OUT>> {

    String PARAM_ID = "/{id}";

    ControllerProvider<IN, OUT, S> controllerProvider();

    default S service() {
        return controllerProvider().service();
    }

    default Mappable<IN, OUT> mappable() {
        return controllerProvider().mappable();
    }

    default OUT mappableTo(IN identifiable) {
        return controllerProvider().mappableTo(identifiable);
    }

    default IN mappableOf(OUT identifiable) {
        return controllerProvider().mappableOf(identifiable);
    }

    default Class<IN> inClass() {
        return HelperClass.identifyGenericsClass(this, 0);
    }

    default Class<OUT> outClass() {
        return HelperClass.identifyGenericsClass(this, 1);
    }
}