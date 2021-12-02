package com.lsv.lib.core.behavior;

import lombok.NonNull;

public interface Updatable<T extends Identifiable<?>> {

    T update(@NonNull T objIdentifiable);
}