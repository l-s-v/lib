package com.lsv.lib.spring.web.test.controller.reactive;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.spring.web.test.controller.TestControllerCreatable;
import org.junit.jupiter.api.function.Executable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public interface TestControllerCreatableWebClient<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT> & Creatable<OUT>>
        extends
        TestControllerCreatable<IN, OUT, S>,
        TestControllerWebClient<IN, OUT, S> {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    default Executable execCreate() {
        return () -> {
            when(service().create(any()))
                    .thenReturn(expectedObjectOut());

            webTestClient()
                    .post()
                    .uri(urlBase())
                    .bodyValue(newObjectIn())
                    .exchange()
                    .expectStatus().isCreated();
        };
    }

    @Override
    default Executable execCreateWithObjectInvalid() {
        return () -> {
            webTestClient()
                    .post()
                    .uri(urlBase())
                    .bodyValue(newObjectInInvalidForCreateAndUpdate())
                    .exchange()
                    .expectStatus().isBadRequest();
        };
    }
}