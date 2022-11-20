package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.*;
import com.lsv.lib.core.concept.repository.Repository;

public interface CrudService<
        I extends Identifiable<?>,
        R extends Repository<I> & Creatable<I> & Readable<I> & Updatable<I> & Deletable<I>>
        extends
        CreatableService<I, R>,
        UpdatableService<I, R>,
        DeletableService<I, R>,
        ReadableService<I, R> {
}