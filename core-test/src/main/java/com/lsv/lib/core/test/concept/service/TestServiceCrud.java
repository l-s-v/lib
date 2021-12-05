package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Crud;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.Service;
import org.junit.jupiter.api.DynamicTest;

import java.util.HashSet;
import java.util.stream.Stream;

public interface TestServiceCrud
    <
        D extends Identifiable<?>,
        S extends Service<D> & Crud<D>,
        R extends Repository<D> & Crud<D>>
    extends
    TestServiceCreatable<D, S, R>,
    TestServiceUpdatable<D, S, R>,
    TestServiceDeletable<D, S, R>,
    TestServiceReadable<D, S, R> {

    @Override
    default Stream<DynamicTest> of() {
        HashSet<String> uniqueNames = new HashSet<>();

        return Stream.of(
                TestServiceCreatable.super.of(),
                TestServiceReadable.super.of(),
                TestServiceUpdatable.super.of(),
                TestServiceDeletable.super.of())
            .flatMap(o -> o)
            .filter(dynamicTest -> uniqueNames.add(dynamicTest.getDisplayName()));
    }
}