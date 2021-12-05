package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.behavior.Crud;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import org.junit.jupiter.api.DynamicTest;

import java.util.HashSet;
import java.util.stream.Stream;

public interface TestRepositoryCrud
    <
        D extends Identifiable<?>,
        R extends Repository<?> & Crud<D>>
    extends
    TestRepositoryCreatable<D, R>,
    TestRepositoryReadable<D, R>,
    TestRepositoryUpdatable<D, R>,
    TestRepositoryDeletable<D, R> {

    @Override
    default Stream<DynamicTest> of() {
        HashSet<String> uniqueNames = new HashSet<>();

        return Stream.of(
                TestRepositoryCreatable.super.of(),
                TestRepositoryReadable.super.of(),
                TestRepositoryUpdatable.super.of(),
                TestRepositoryDeletable.super.of())
            .flatMap(o -> o)
            .filter(dynamicTest -> uniqueNames.add(dynamicTest.getDisplayName()));
    }
}