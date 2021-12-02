package com.lsv.lib.core.behavior;

import lombok.NonNull;

public interface Updateable<T extends Identifiable<?>> {

    T update(@NonNull T objIdentifiable);
}