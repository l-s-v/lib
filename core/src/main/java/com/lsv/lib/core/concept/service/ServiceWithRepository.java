package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;

public interface ServiceWithRepository<
    I extends Identifiable<?>,
    R extends Repository<I>>
    extends
    Service<I> {

    ServiceProvider<I, R> serviceProvider();
}