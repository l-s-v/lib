package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.test.TestWithMockito;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

public interface TestServiceWithRepository<
    I extends Identifiable<?>,
    S extends Service<I>,
    R extends Repository<I>>
    extends
    TestWithMockito {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
            Stream.of(serviceNoSuchElementException()),
            TestWithMockito.super.of());
    }

    @SuppressWarnings("unchecked")
    default R repositoryMock() {
        return (R) Mockito.mock(HelperClass.identifyGenericsClass(this, Repository.class));
    }

    @SuppressWarnings({"rawtypes"})
    default S serviceImpl(R repository) {
        /*
         * Simulates Repository.findInstance so that it is not necessary to
         * define the dependency of some implementation inside the module.
         * It was the possible way to test the automatic functioning (by service module)
         * because the instances are created with default constructors.
         * */
        try (MockedStatic<Repository> repositoryMockedStatic = Mockito.mockStatic(Repository.class)) {
            repositoryMockedStatic
                .when(() -> Repository.findInstance(Mockito.any()))
                .thenReturn(repository);

            return Service.findInstance(this);
        }
    }

    default S service(R repository) {
        return serviceImpl(repository);
    }

    default void acessDefault() {
        Repository.findInstance(this);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicNode serviceNoSuchElementException() {
        return DynamicTest.dynamicTest("serviceNoSuchElementException", () -> {
            Assertions.assertThrows(NoSuchElementException.class, this::acessDefault);
        });
    }
}