package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Deleteable;
import com.lsv.lib.core.behavior.Identifiable;

public interface DeleteableService<T extends Identifiable<?>, R extends Deleteable<T>>
        extends ServiceWithRepository<T, R>, Deleteable<T> {

    default void delete(T registro) {
        this.repository().delete(registro);
    }
}