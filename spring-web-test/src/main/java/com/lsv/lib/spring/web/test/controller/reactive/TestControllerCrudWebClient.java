package com.lsv.lib.spring.web.test.controller.reactive;

import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.*;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.DynamicNode;

import java.util.stream.Stream;

public interface TestControllerCrudWebClient<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Creatable<OUT> & Readable<OUT> & Updatable<OUT> & Deletable<OUT>>
        extends
        TestControllerCreatableWebClient<IN, OUT, S>,
        TestControllerReadableWebClient<IN, OUT, S>,
        TestControllerUpdatableWebClient<IN, OUT, S>,
        TestControllerDeletableWebClient<IN, OUT, S> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
                TestControllerCreatableWebClient.super.of(),
                TestControllerReadableWebClient.super.of(),
                TestControllerUpdatableWebClient.super.of(),
                TestControllerDeletableWebClient.super.of()
        );
    }
}