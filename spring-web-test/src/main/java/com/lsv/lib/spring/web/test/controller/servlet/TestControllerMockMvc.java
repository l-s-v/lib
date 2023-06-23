package com.lsv.lib.spring.web.test.controller.servlet;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.test.TestForFactory;
import lombok.SneakyThrows;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

public interface TestControllerMockMvc<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S>
        extends
        TestControllerProviderMockMvc<IN, OUT, S>,
        TestForFactory {

    @SneakyThrows
    default ResultActions performInContext(RequestBuilder requestBuilder) {
        return mockMvc().perform(requestBuilder);
    }
}