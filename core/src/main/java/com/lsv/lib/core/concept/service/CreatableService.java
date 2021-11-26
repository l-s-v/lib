package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;

public interface CreatableService<T extends Identifiable<?>, R extends Creatable<T>> extends ServiceWithRepository<T, R>, Creatable<T> {

    default T insert(T registro) {
        return this.repository().insert(registro);
    }
}