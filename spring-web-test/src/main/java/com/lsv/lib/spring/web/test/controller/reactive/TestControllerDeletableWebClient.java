package com.lsv.lib.spring.web.test.controller.reactive;

import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.spring.web.test.controller.TestControllerDeletable;
import org.junit.jupiter.api.function.Executable;

public interface TestControllerDeletableWebClient<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT> & Deletable<OUT>>
        extends
        TestControllerDeletable<IN, OUT, S>,
        TestControllerWebClient<IN, OUT, S> {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    default Executable execDelete() {
        return () -> {
            webTestClient()
                    .delete()
                    .uri(urlBaseWithId(), newObjectIn().getId().toString())
                    .exchange()
                    .expectStatus().isOk();
        };
    }
}