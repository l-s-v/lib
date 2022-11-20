package com.lsv.lib.spring.web.test.controller.servlet;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.spring.web.test.controller.TestControllerUpdatable;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface TestControllerUpdatableMockMvc<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT> & Updatable<OUT>>
        extends
        TestControllerUpdatable<IN, OUT, S>,
        TestControllerMockMvc<IN, OUT, S> {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    default Executable execUpdate() {
        return () -> {
            when(service().update(any()))
                    .thenReturn(expectedObjectOut());

            IN in = newObjectIn();

            performInContext(
                    put(urlBaseWithId(), in.getId().toString())
                            .content(objectMapper().writeValueAsString(in))
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andExpect(status().isOk());
        };
    }

    @Override
    default Executable execUpdateWithObjectInvalid() {
        return () -> {
            performInContext(
                    put(urlBaseWithId(), newObjectIn().getId().toString())
                            .content(objectMapper().writeValueAsString(newObjectInInvalidForCreateAndUpdate()))
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        };
    }
}