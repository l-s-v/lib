package com.lsv.lib.spring.web.test.controller.servlet;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.spring.web.test.controller.TestControllerProvider;
import org.springframework.test.web.servlet.MockMvc;

public interface TestControllerProviderMockMvc<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S>
        extends TestControllerProvider<IN, OUT, S> {

    MockMvc mockMvc();
}