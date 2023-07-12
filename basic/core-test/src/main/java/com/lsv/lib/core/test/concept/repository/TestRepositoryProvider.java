package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;

public interface TestRepositoryProvider<D extends Identifiable<?>> {

    D newObjectCompleteWithoutId();
}