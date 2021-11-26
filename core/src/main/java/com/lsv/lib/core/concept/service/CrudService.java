package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Crud;
import com.lsv.lib.core.behavior.Identifiable;

public interface CrudService<T extends Identifiable<?>, R extends Crud<T>> extends
        ServiceWithRepository<T, R>,
        CreatableService<T, R>,
        UpdateableService<T, R>,
        DeleteableService<T, R>,
        ReadableService<T, R> {}