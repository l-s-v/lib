package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.concept.service.ServiceWithRepository;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.pattern.register.RegisterInterface;
import com.lsv.lib.core.test.TestWithMockito;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.mockito.Mockito;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

public interface TestServiceWithRepository
    <
        D extends Identifiable<?>,
        S extends Service<D>,
        R extends Repository<D>>
    extends
    TestWithMockito {

    default Stream<DynamicTest> of() {
        return Stream.of(
            this.forceInformRepository(),
            this.serviceNoSuchElementException()
        );
    }

    @SuppressWarnings("unchecked")
    default R repositoryMock() {
        return (R) Mockito.mock(HelperClass.identifyGenericsClass(this, Repository.class));
    }

    @SuppressWarnings("unchecked")
    default ServiceWithRepository<D, R> serviceImpl() {
        return (ServiceWithRepository<D, R>) RegisterInterface.findImplementation(
            HelperClass.identifyGenericsClass(this, Service.class));
    }

    @SuppressWarnings("unchecked")
    default S service(R repository) {
        return (S) serviceImpl().repository(repository);
    }

    default void acessRepositoryDefault() {
        serviceImpl().repository();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicTest forceInformRepository() {
        return DynamicTest.dynamicTest("forceInformRepository", () -> {
            Assertions.assertThrows(NullPointerException.class, () -> service(null));
        });
    }

    private DynamicTest serviceNoSuchElementException() {
        return DynamicTest.dynamicTest("serviceNoSuchElementException", () -> {
            Assertions.assertThrows(NoSuchElementException.class, this::acessRepositoryDefault);
        });
    }
}