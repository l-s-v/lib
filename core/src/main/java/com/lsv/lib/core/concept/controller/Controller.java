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

    default S service() {
        return (S) Service.findInstance(this, Service.class);
    }

    default Mappable<IN, OUT> mappable() {
        return (Mappable<IN, OUT>) Mappable.findInstance(this, inClass(), outClass());
    }

    default OUT mappableTo(IN identifiable) {
        return mappable().to(identifiable);
    }

    default IN mappableOf(OUT identifiable) {
        return mappable().of(identifiable);
    }

    default Class<IN> inClass() {
        return HelperClass.identifyGenericsClass(this, 0);
    }

    default Class<OUT> outClass() {
        return HelperClass.identifyGenericsClass(this, 1);
    }

    String urlBase();
}