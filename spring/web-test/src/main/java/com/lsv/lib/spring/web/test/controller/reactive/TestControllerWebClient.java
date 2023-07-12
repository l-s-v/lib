package com.lsv.lib.spring.web.test.controller.reactive;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.test.TestForFactory;

public interface TestControllerWebClient<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S>
        extends
        TestControllerProviderWebClient<IN, OUT, S>,
        TestForFactory {
}