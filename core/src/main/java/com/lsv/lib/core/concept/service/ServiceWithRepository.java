package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;

public interface ServiceWithRepository<T extends Identifiable<?>, R> extends Service<T> {

    R repository();
}