package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Identifiable;

public interface ProviderTestService<D extends Identifiable<?>> {

    D newObjectCompleteWithoutId();
    D newObjectComplete();
    D newObjectWithId();
}