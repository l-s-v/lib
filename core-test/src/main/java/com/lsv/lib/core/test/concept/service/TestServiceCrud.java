package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Crud;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.Service;

public interface TestServiceCrud
    <
        D extends Identifiable<?>,
        S extends Service<D> & Crud<D>,
        R extends Repository<D> & Crud<D>>
    extends
    TestServiceCreatable<D, S, R>,
    TestServiceUpdateable<D, S, R>,
    TestServiceDeletable<D, S, R>,
    TestServiceReadable<D, S, R> {
}