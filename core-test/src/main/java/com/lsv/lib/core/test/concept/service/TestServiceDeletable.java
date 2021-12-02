package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public interface TestServiceDeletable
    <
        D extends Identifiable<?>,
        S extends Service<D> & Deletable<D>,
        R extends Repository<D> & Deletable<D>>
    extends
    TestServiceWithRepository<D, S, R>,
    ProviderTestService<D> {

    @Test
    default public void delete() {
        Assertions.assertDoesNotThrow(() -> service(repositoryMock()).delete(newObjectWithId()));
    }
}