package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

public interface TestServiceDeletable<
        I extends Identifiable<?>,
        S extends Deletable<I>,
        R extends Deletable<I>>
        extends
        TestServiceWithRepository<I, S, R>,
        TestServiceProvider<I> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
                Stream.of(delete()),
                TestServiceWithRepository.super.of());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicNode delete() {
        return DynamicTest.dynamicTest("delete", () -> {
            lenient().doNothing().when(repository()).delete(any());
            Assertions.assertDoesNotThrow(() -> service().delete(newObjectWithId()));
        });
    }
}