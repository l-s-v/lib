package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.Validable;

import java.util.List;

public interface ServiceWithRepository<
        I extends Identifiable<?>,
        R extends Repository<I>>
        extends
        Service<I> {

    default R repository() {
        return (R) Repository.findInstance(this, Repository.class);
    }

    List<Validable<I>> validables();
}