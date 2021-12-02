package com.lsv.lib.core.behavior;

import lombok.NonNull;

public interface Deleteable<T extends Identifiable<?>> {

    void delete(@NonNull T objIdentifiable);
}