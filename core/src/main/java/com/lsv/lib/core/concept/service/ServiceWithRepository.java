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

    ServiceProvider<I, R> serviceProvider();

    default R repository() {
        return serviceProvider().repository();
    }

    default List<Validable<I>> validables() {
        return serviceProvider().validables();
    }
}