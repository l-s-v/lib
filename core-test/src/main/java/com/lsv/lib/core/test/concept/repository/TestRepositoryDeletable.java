package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.repository.Repository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

public interface TestRepositoryDeletable
    <
        D extends Identifiable<?>,
        R extends Repository<?> & Creatable<D> & Deletable<D> & Readable<D>>
    extends
    TestRepository<R>,
    TestRepositoryProvider<D> {

    @Override
    default Stream<DynamicTest> of() {
        return Stream.of(
                Stream.of(this.delete()),
                TestRepository.super.of())
            .flatMap(o -> o);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicTest delete() {
        return DynamicTest.dynamicTest("delete", () -> {
            R repository = repository();
            D obj = repository.create(newObjectCompleteWithoutId());
            repository.delete(obj);
            Assertions.assertFalse(repository.findById(obj).isPresent());
        });
    }
}