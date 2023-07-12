package com.lsv.lib.core.test;

import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public interface TestDynamic {

    @DisplayName("DYNAMIC TESTS")
    @TestFactory
    default Stream<DynamicNode> dynamicTest() {
        return HelperDynamicTest.findTestForFactory(this.getClass());
    }

    default Stream<DynamicNode> dynamicTestPackage() {
        return HelperDynamicTest.findTestForFactory(this.getClass().getPackageName());
    }
}