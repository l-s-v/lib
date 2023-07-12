package com.lsv.lib.spring.web.test.controller.servlet;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.spring.web.test.controller.TestControllerCreatable;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface TestControllerCreatableMockMvc<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Creatable<OUT>>
        extends
        TestControllerCreatable<IN, OUT, S>,
        TestControllerMockMvc<IN, OUT, S> {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    default Executable execCreate() {
        return () -> {
            when(service().create(any()))
                    .thenReturn(expectedObjectOut());

            performInContext(
                    post(urlBase())
                            .content(objectMapper().writeValueAsString(newObjectIn()))
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andExpect(status().isCreated());
        };
    }

    @Override
    default Executable execCreateWithObjectInvalid() {
        return () -> {
            performInContext(
                    post(urlBase())
                            .content(objectMapper().writeValueAsString(newObjectInInvalidForCreateAndUpdate()))
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        };
    }
}