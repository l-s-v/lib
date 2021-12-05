package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

public interface TestServiceDeletable
    <
        D extends Identifiable<?>,
        S extends Service<D> & Deletable<D>,
        R extends Repository<D> & Deletable<D>>
    extends
    TestServiceWithRepository<D, S, R>,
    TestServiceProvider<D> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
            Stream.of(this.delete()),
            TestServiceWithRepository.super.of());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicNode delete() {
        return DynamicTest.dynamicTest("delete", () -> {
            Assertions.assertDoesNotThrow(() -> service(repositoryMock()).delete(newObjectWithId()));
        });
    }
}