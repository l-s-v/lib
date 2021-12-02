package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;

import java.io.Serializable;

public interface CrudRepository<T extends Identifiable<ID>, ID extends Serializable, P extends Persistable<?>> extends
        CreatableRepository<T, ID, P>,
        UpdateableRepository<T, ID, P>,
        DeleteableRepository<T, ID, P>,
        ReadableRepository<T, ID, P> {}