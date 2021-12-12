package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

public interface TestServiceCreatable<
    I extends Identifiable<?>,
    S extends Service<I> & Creatable<I>,
    R extends Repository<I> & Creatable<I>>
    extends
    TestServiceWithRepository<I, S, R>,
    TestServiceProvider<I> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
            Stream.of(create()),
            TestServiceWithRepository.super.of());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicNode create() {
        return DynamicTest.dynamicTest("create", () -> {
            R repositoryMock = repositoryMock();

            lenient().when(repositoryMock.create(any()))
                .thenReturn(newObjectWithId());

            I obj = newObjectCompleteWithoutId();
            Assertions.assertNotNull(service(repositoryMock).create(obj).getId());
        });
    }
}