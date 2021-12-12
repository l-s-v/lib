package com.lsv.lib.spring.web.test.controller;

import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.*;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.DynamicNode;

import java.util.stream.Stream;

public interface TestControllerCrud<
    IN extends Identifiable<?>,
    OUT extends Identifiable<?>,
    S extends Service<OUT> & Creatable<OUT> & Readable<OUT> & Updatable<OUT> & Deletable<OUT>>
    extends
    TestControllerCreatable<IN, OUT, S>,
    TestControllerReadable<IN, OUT, S>,
    TestControllerUpdatable<IN, OUT, S>,
    TestControllerDeletable<IN, OUT, S> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
            TestControllerCreatable.super.of(),
            TestControllerReadable.super.of(),
            TestControllerUpdatable.super.of(),
            TestControllerDeletable.super.of()
        );
    }
}