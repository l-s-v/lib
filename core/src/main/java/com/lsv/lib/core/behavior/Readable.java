package com.lsv.lib.core.behavior;

import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import lombok.NonNull;

public interface Readable<T extends Identifiable<?>> {

    T findById(@NonNull T registro);
    ListDto<T> findByFilter(@NonNull Filter<T> filter);
}