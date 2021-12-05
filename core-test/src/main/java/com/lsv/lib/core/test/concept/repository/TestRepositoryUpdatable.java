package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.repository.Repository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

public interface TestRepositoryUpdatable
    <
        D extends Identifiable<?>,
        R extends Repository<?> & Creatable<D> & Updatable<D> & Readable<D>>
    extends
    TestRepository<R>,
    TestRepositoryProvider<D> {

    @Override
    default Stream<DynamicTest> of() {
        return Stream.of(
                Stream.of(this.update()),
                TestRepository.super.of())
            .flatMap(o -> o);
    }

    D modifyObjectForUpdateTest(D obj);

    boolean verifyObjectForUpdateTest(D obj);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicTest update() {
        return DynamicTest.dynamicTest("update", () -> {
            R repository = repository();

            // TODO: refatorar para uma chamada
            D obj = repository.create(newObjectCompleteWithoutId());
            obj = modifyObjectForUpdateTest(obj);
            repository.update(obj);
            obj = repository.findById(obj).orElseThrow();

            Assertions.assertTrue(verifyObjectForUpdateTest(obj));
        });
    }
}