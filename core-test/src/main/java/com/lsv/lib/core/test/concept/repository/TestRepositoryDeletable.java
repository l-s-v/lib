package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

public interface TestRepositoryDeletable
    <
        D extends Identifiable<?>,
        R extends Repository<D> & Creatable<D> & Deletable<D> & Readable<D>>
    extends
    TestRepository<D, R>,
    TestRepositoryProvider<D> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
            Stream.of(this.delete()),
            TestRepository.super.of());
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