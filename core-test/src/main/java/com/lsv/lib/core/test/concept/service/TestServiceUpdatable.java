package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.mockito.AdditionalAnswers;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

public interface TestServiceUpdatable
    <
        D extends Identifiable<?>,
        S extends Service<D> & Updatable<D>,
        R extends Repository<D> & Updatable<D>>
    extends
    TestServiceWithRepository<D, S, R>,
    TestServiceProvider<D> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
            Stream.of(this.update()),
            TestServiceWithRepository.super.of());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicNode update() {
        return DynamicTest.dynamicTest("update", () -> {
            R repositoryMock = repositoryMock();

            lenient().when(repositoryMock.update(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

            D obj = newObjectComplete();
            Assertions.assertEquals(obj, service(repositoryMock).update(obj));
        });
    }
}