package com.lsv.lib.core.behavior;

import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import lombok.NonNull;

import java.util.Optional;

public interface Readable<T extends Identifiable<?>> {

    Optional<T> findById(@NonNull T objIdentifiable);
    ListDto<T> findByFilter(@NonNull Filter<T> filter);
}