package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;

import java.io.Serializable;

public interface CrudRepository<I extends Identifiable<ID>, ID extends Serializable, P extends Persistable<?>> extends
        CreatableRepository<I, ID, P>,
        UpdateableRepository<I, ID, P>,
        DeleteableRepository<I, ID, P>,
        ReadableRepository<I, ID, P> {
}