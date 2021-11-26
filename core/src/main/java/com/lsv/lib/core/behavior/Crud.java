package com.lsv.lib.core.behavior;

public interface Crud<T extends Identifiable<?>> extends
        Creatable<T>,
        Updateable<T>,
        Deleteable<T>,
        Readable<T> {}