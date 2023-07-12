package com.lsv.lib.spring.web.test.controller.reactive;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.spring.web.test.controller.AbstractTestController;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@Getter
@WebFluxTest
public abstract class AbstractTestControllerWebClient<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S>
        extends
        AbstractTestController<IN, OUT, S>
        implements
        TestControllerWebClient<IN, OUT, S> {

    @Autowired
    private WebTestClient webTestClient;
}