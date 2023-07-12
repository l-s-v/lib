package com.lsv.lib.spring.web.test.controller.servlet;

import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.*;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.DynamicNode;

import java.util.stream.Stream;

public interface TestControllerCrudMockMvc<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Creatable<OUT> & Readable<OUT> & Updatable<OUT> & Deletable<OUT>>
        extends
        TestControllerCreatableMockMvc<IN, OUT, S>,
        TestControllerReadableMockMvc<IN, OUT, S>,
        TestControllerUpdatableMockMvc<IN, OUT, S>,
        TestControllerDeletableMockMvc<IN, OUT, S> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
                TestControllerCreatableMockMvc.super.of(),
                TestControllerReadableMockMvc.super.of(),
                TestControllerUpdatableMockMvc.super.of(),
                TestControllerDeletableMockMvc.super.of()
        );
    }
}