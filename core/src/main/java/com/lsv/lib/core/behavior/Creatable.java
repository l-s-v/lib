package com.lsv.lib.core.behavior;

import lombok.NonNull;

public interface Creatable<T extends Identifiable<?>> {

    T insert(@NonNull T registro);
}