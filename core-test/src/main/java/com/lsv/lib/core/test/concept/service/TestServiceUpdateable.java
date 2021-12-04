package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

public interface TestServiceUpdateable
    <
        D extends Identifiable<?>,
        S extends Service<D> & Updatable<D>,
        R extends Repository<D> & Updatable<D>>
    extends
    TestServiceWithRepository<D, S, R>,
    ProviderTestService<D> {

    @Test
    default void update() {
        R repositoryMock = repositoryMock();

        lenient().when(repositoryMock.update(any()))
            .thenAnswer(AdditionalAnswers.returnsFirstArg());

        D obj = newObjectComplete();
        Assertions.assertEquals(obj, service(repositoryMock).update(obj));
    }
}