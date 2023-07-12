package com.lsv.lib.core.behavior;

import lombok.NonNull;

public interface Deletable<T extends Identifiable<?>> {

    void delete(@NonNull T identifiable);
}