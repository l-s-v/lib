package com.lsv.lib.spring.web.test.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.test.TestForFactory;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;

import java.util.stream.Stream;

public interface TestControllerUpdatable<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT> & Updatable<OUT>>
        extends
        TestForFactory {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
                Stream.of(
                        update(),
                        updateWithObjectInvalid()),
                TestForFactory.super.of());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    Executable execUpdate();

    Executable execUpdateWithObjectInvalid();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicTest update() {
        return DynamicTest.dynamicTest("update", execUpdate());
    }

    private DynamicTest updateWithObjectInvalid() {
        return DynamicTest.dynamicTest("updateWithObjectInvalid", execUpdateWithObjectInvalid());
    }
}