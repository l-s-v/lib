package com.lsv.lib.spring.web.test.controller;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.test.TestForFactory;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;

import java.util.stream.Stream;

public interface TestControllerCreatable<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT> & Creatable<OUT>>
        extends
        TestForFactory {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
                Stream.of(
                        create(),
                        createWithObjectInvalid()),
                TestForFactory.super.of());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    Executable execCreate();

    Executable execCreateWithObjectInvalid();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicTest create() {
        return DynamicTest.dynamicTest("create", execCreate());
    }

    private DynamicTest createWithObjectInvalid() {
        return DynamicTest.dynamicTest("createWithObjectInvalid", execCreateWithObjectInvalid());
    }
}