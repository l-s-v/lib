package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

public interface TestRepositoryCreatable<
    D extends Identifiable<?>,
    R extends Repository<?> & Creatable<D>>
    extends
    TestRepository<R>,
    TestRepositoryProvider<D> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
            Stream.of(this.create()),
            TestRepository.super.of());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicTest create() {
        return DynamicTest.dynamicTest("create", () -> {
            Assertions.assertNotNull(repository().create(newObjectCompleteWithoutId()).getId());
        });
    }
}