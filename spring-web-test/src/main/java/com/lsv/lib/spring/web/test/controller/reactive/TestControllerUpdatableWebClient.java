package com.lsv.lib.spring.web.test.controller.reactive;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updatable;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.spring.web.test.controller.TestControllerUpdatable;
import org.junit.jupiter.api.function.Executable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public interface TestControllerUpdatableWebClient<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT> & Updatable<OUT>>
        extends
        TestControllerUpdatable<IN, OUT, S>,
        TestControllerWebClient<IN, OUT, S> {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    default Executable execUpdate() {
        return () -> {
            when(service().update(any()))
                    .thenReturn(expectedObjectOut());

            IN in = newObjectIn();

            webTestClient()
                    .put()
                    .uri(urlBaseWithId(), in.getId().toString())
                    .bodyValue(in)
                    .exchange()
                    .expectStatus().isOk();
        };
    }

    @Override
    default Executable execUpdateWithObjectInvalid() {
        return () -> {
            webTestClient()
                    .put()
                    .uri(urlBaseWithId(), newObjectIn().getId().toString())
                    .bodyValue(newObjectInInvalidForCreateAndUpdate())
                    .exchange()
                    .expectStatus().isBadRequest();
        };
    }
}