package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

public interface TestRepositoryUpdatable<
        D extends Identifiable<?>,
        R extends Repository<D> & Creatable<D> & Updatable<D> & Readable<D>>
        extends
        TestRepository<D, R> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
                Stream.of(this.update()),
                TestRepository.super.of());
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