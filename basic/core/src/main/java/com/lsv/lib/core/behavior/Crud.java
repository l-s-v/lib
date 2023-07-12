package com.lsv.lib.core.behavior;

public interface Crud<T extends Identifiable<?>> extends
        Creatable<T>,
        Updatable<T>,
        Deletable<T>,
        Readable<T> {
}