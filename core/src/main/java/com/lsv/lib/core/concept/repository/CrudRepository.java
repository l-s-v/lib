package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;

public interface CrudRepository<T extends Identifiable<?>> extends
        CreatableRepository<T>,
        UpdateableRepository<T>,
        DeleteableRepository<T>,
        ReadableRepository<T> {}