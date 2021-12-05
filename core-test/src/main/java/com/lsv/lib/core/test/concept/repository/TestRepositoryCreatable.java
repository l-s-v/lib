package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

public interface TestRepositoryCreatable<
    D extends Identifiable<?>,
    R extends Repository<?> & Creatable<D>>
    extends
    TestRepository<R>,
    TestRepositoryProvider<D> {

    @Override
    default Stream<DynamicTest> of() {
        return Stream.of(
                Stream.of(this.create()),
                TestRepository.super.of())
            .flatMap(o -> o);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicTest create() {
        return DynamicTest.dynamicTest("create", () -> {
            Assertions.assertNotNull(repository().create(newObjectCompleteWithoutId()).getId());
        });
    }
}