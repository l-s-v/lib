package com.lsv.lib.spring.web.test.controller;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface TestControllerCreatable<
    IN extends Identifiable<?>,
    OUT extends Identifiable<?>,
    S extends Service<OUT> & Creatable<OUT>>
    extends
    TestController<IN, OUT, S> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
            Stream.of(
                create(),
                createWithObjectInvalid()),
            TestController.super.of());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicTest create() {
        return DynamicTest.dynamicTest("create", () -> {
            when(serviceMock().create(any()))
                .thenReturn(expectedObjectOut());

            String objJson = objectMapper().writeValueAsString(newObjectIn());

            performInContext(
                post(urlBase())
                    .content(objJson)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objJson));
        });
    }

    private DynamicTest createWithObjectInvalid() {
        return DynamicTest.dynamicTest("createWithObjectInvalid", () -> {
            performInContext(
                post(urlBase())
                    .content(objectMapper().writeValueAsString(newObjectInInvalidForCreateAndUpdate()))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
        });
    }
}