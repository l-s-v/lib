package com.lsv.lib.core.behavior;

import lombok.NonNull;

public interface Creatable<T extends Identifiable<?>> {

    T create(@NonNull T registro);
}