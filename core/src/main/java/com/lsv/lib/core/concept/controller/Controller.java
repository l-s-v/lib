package com.lsv.lib.core.concept.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.concept.service.Service;

public interface Controller<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT>> {

    String PARAM_ID = "/{id}";

    S service();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    default Mappable<IN, OUT> mappable() {
        return null;
    }

    default OUT mappableTo(IN identifiable) {
        return mappable().to(identifiable);
    }

    default IN mappableOf(OUT identifiable) {
        return mappable().of(identifiable);
    }
}