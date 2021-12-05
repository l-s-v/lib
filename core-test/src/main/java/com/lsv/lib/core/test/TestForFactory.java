package com.lsv.lib.core.test;

import org.junit.jupiter.api.DynamicNode;

import java.util.stream.Stream;

public interface TestForFactory {

    default Stream<DynamicNode> of() {
        return Stream.empty();
    }
}