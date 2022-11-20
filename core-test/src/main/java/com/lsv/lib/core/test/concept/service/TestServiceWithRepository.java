package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.test.TestWithMockito;

public interface TestServiceWithRepository<
        I extends Identifiable<?>,
        S extends Service<I>,
        R extends Repository<I>>
        extends
        TestWithMockito {

    @SuppressWarnings("unchecked")
    default R repository() {
        return (R) Repository.findInstance(this, Repository.class);
    }

    default S service() {
        return (S) Service.findInstance(this, Service.class);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
}