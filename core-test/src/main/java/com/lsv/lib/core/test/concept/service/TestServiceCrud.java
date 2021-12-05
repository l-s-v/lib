package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Crud;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.DynamicNode;

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
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
            TestServiceCreatable.super.of(),
            TestServiceReadable.super.of(),
            TestServiceUpdatable.super.of(),
            TestServiceDeletable.super.of());
    }
}