package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.test.TestWithMockito;

public interface TestServiceWithRepository<
        I extends Identifiable<?>,
        S,
        R>
        extends
        TestWithMockito {

    R repository();

    S service();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
}