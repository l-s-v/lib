package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;

public interface ReadableRepository<T extends Identifiable<?>> extends Repository<T>, Readable<T> {

    T findById(T registro);
    ListDto<T> findByFilters(Filter<T> filter);
}