package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

public interface TestServiceCreatable
    <
        D extends Identifiable<?>,
        S extends Service<D> & Creatable<D>,
        R extends Repository<D> & Creatable<D>>
    extends
    TestServiceWithRepository<D, S, R>,
    ProviderTestService<D> {

    @Test
    default public void insert() {
        R repositoryMock = repositoryMock();

        lenient().when(repositoryMock.create(any()))
            .thenAnswer(args -> newObjectWithId());

        D obj = newObjectCompleteWithoutId();
        Assertions.assertNotNull(service(repositoryMock).create(obj).getId());
    }
}