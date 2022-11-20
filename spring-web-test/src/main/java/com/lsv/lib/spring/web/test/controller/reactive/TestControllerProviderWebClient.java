package com.lsv.lib.spring.web.test.controller.reactive;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.spring.web.test.controller.TestControllerProvider;
import org.springframework.test.web.reactive.server.WebTestClient;

public interface TestControllerProviderWebClient<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT>>
        extends TestControllerProvider<IN, OUT, S> {

    WebTestClient webTestClient();
}