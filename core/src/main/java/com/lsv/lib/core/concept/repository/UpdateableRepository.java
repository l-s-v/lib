package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Updateable;

public interface UpdateableRepository<T extends Identifiable<?>> extends Repository<T>, Updateable<T> {

    T update(T registro);
}