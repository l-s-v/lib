package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

public interface TestRepositoryReadable<
        D extends Identifiable<?>,
        R extends Repository<D> & Creatable<D> & Readable<D>>
        extends
        TestRepository<D, R> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
                Stream.of(this.findById(), this.findByFilter()),
                TestRepository.super.of());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicTest findById() {
        return DynamicTest.dynamicTest("findById", () -> {
            R repository = repository();
            D obj = repository.create(newObjectCompleteWithoutId());
            Assertions.assertTrue(repository.findById(obj).isPresent());
        });
    }

    private DynamicTest findByFilter() {
        return DynamicTest.dynamicTest("findByFilter", () -> {
            R repository = repository();

            D obj = null;
            for (int i = 0; i < 6; i++) {
                obj = repository.create(newObjectCompleteWithoutId());
            }

            Assertions.assertEquals(1, repository.findByFilter(Filter.of(obj).get()).size());
        });
    }
}