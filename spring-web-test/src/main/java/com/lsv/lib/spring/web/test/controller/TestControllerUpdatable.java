package com.lsv.lib.spring.web.test.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface TestControllerUpdatable<
    IN extends Identifiable<?>,
    OUT extends Identifiable<?>,
    S extends Service<OUT> & Updatable<OUT>>
    extends
    TestController<IN, OUT, S> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
            Stream.of(update()),
            TestController.super.of());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicTest update() {
        return DynamicTest.dynamicTest("update", () -> {
            when(serviceMock().update(any()))
                .thenReturn(expectedObjectOut());

            String objJson = objectMapper().writeValueAsString(newObjectIn());

            performInContext(put(urlBase()).content(objJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objJson));
        });
    }
}