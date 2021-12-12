package com.lsv.lib.spring.web.test.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.test.TestForFactory;
import lombok.SneakyThrows;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;

public interface TestController<
    IN extends Identifiable<?>,
    OUT extends Identifiable<?>,
    S extends Service<OUT>>
    extends
    TestControllerProvider<IN, OUT, S>,
    TestForFactory {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @SneakyThrows
    default ResultActions performInContext(RequestBuilder requestBuilder) {
        /*
         * Simulates Mappable.findInstance so that it is not necessary to
         * define the dependency of some implementation inside the module.
         * It was the possible way to test the automatic functioning (by service module)
         * because the instances are created with default constructors.
         * */
        try (MockedStatic<Mappable> mappableMockedStatic = Mockito.mockStatic(Mappable.class)) {
            mappableMockedStatic
                .when(() -> Mappable.findInstance(any(), any()))
                .thenAnswer(invocation -> mappable().setup(
                    (Class<IN>) invocation.getArguments()[0],
                    (Class<OUT>) invocation.getArguments()[1]
                ));

            return mockMvc().perform(requestBuilder);
        }
    }
}