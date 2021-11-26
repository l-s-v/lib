package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updateable;

public interface UpdateableService<T extends Identifiable<?>, R extends Updateable<T>>
        extends ServiceWithRepository<T, R>, Updateable<T> {

    default T update(T registro) {
        return this.repository().update(registro);
    }
}