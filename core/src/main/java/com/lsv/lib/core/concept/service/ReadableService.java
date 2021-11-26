package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;

public interface ReadableService<T extends Identifiable<?>, R extends Readable<T>>
        extends ServiceWithRepository<T, R>, Readable<T> {

    default T findById(T registro) {
        return this.repository().findById(registro);
    }

    default ListDto<T> findByFilters(Filter<T> filter) {
        return this.repository().findByFilters(filter);
    }
}