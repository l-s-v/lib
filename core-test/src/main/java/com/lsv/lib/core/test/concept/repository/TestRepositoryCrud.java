package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.behavior.Crud;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.DynamicNode;

import java.util.stream.Stream;

public interface TestRepositoryCrud
    <
        D extends Identifiable<?>,
        R extends Repository<D> & Crud<D>>
    extends
    TestRepositoryCreatable<D, R>,
    TestRepositoryReadable<D, R>,
    TestRepositoryUpdatable<D, R>,
    TestRepositoryDeletable<D, R> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
            TestRepositoryCreatable.super.of(),
            TestRepositoryReadable.super.of(),
            TestRepositoryUpdatable.super.of(),
            TestRepositoryDeletable.super.of());
    }
}