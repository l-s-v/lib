package com.lsv.lib.spring.web.test.controller.servlet;

import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.spring.web.test.controller.TestControllerDeletable;
import org.junit.jupiter.api.function.Executable;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface TestControllerDeletableMockMvc<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Deletable<OUT>>
        extends
        TestControllerDeletable<IN, OUT, S>,
        TestControllerMockMvc<IN, OUT, S> {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    default Executable execDelete() {
        return () -> {
            performInContext(delete(urlBaseWithId(), newObjectIn().getId().toString()))
                    .andDo(print())
                    .andExpect(status().isOk());
        };
    }
}