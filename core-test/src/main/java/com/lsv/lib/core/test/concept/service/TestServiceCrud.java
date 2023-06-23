package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.*;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.DynamicNode;

import java.util.stream.Stream;

public interface TestServiceCrud<
        I extends Identifiable<?>,
        S extends Creatable<I> & Readable<I> & Updatable<I> & Deletable<I>,
        R extends Creatable<I> & Readable<I> & Updatable<I> & Deletable<I>>
        extends
        TestServiceCreatable<I, S, R>,
        TestServiceUpdatable<I, S, R>,
        TestServiceDeletable<I, S, R>,
        TestServiceReadable<I, S, R> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
                TestServiceCreatable.super.of(),
                TestServiceReadable.super.of(),
                TestServiceUpdatable.super.of(),
                TestServiceDeletable.super.of());
    }
}