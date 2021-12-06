package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Crud;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;

public interface CrudService<
    T extends Identifiable<?>,
    R extends Crud<T> & Repository<T>>
    extends
    CreatableService<T, R>,
    UpdatableService<T, R>,
    DeletableService<T, R>,
    ReadableService<T, R> {
}