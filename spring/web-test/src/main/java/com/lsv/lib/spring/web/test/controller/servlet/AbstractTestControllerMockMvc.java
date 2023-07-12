package com.lsv.lib.spring.web.test.controller.servlet;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.spring.web.test.controller.AbstractTestController;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@Getter
@WebMvcTest
public abstract class AbstractTestControllerMockMvc<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S>
        extends
        AbstractTestController<IN, OUT, S>
        implements
        TestControllerMockMvc<IN, OUT, S> {

    @Autowired
    private MockMvc mockMvc;
}