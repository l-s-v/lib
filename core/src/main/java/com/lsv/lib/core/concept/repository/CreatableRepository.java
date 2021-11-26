package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;

public interface CreatableRepository<T extends Identifiable<?>> extends Repository<T>, Creatable<T> {

    T insert(T registro);
}