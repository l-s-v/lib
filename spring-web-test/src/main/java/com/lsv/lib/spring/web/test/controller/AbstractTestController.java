package com.lsv.lib.spring.web.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.test.TestForFactory;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.stream.Stream;

public abstract class AbstractTestController<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT>>
        implements
        TestForFactory {

    @Getter
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private S service;

    @DisplayName("DYNAMIC TESTS")
    @TestFactory
    public Stream<DynamicNode> dynamicTest() {
        return HelperDynamicTest.formatDisplayWithTopAndBottomSeparator(this.getClass(), this.of());
    }
}