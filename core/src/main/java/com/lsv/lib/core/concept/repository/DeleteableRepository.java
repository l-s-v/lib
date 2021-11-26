package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Deleteable;
import com.lsv.lib.core.behavior.Identifiable;

public interface DeleteableRepository<T extends Identifiable<?>> extends Repository<T>, Deleteable<T> {

    void delete(T registro);
}