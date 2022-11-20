package com.lsv.lib.spring.service;

import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.*;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.ReadableService;

public interface CrudServiceTx<
        I extends Identifiable<?>,
        R extends Repository<I> & Creatable<I> & Readable<I> & Updatable<I> & Deletable<I>>
        extends
        CreatableServiceTx<I, R>,
        UpdatableServiceTx<I, R>,
        DeletableServiceTx<I, R>,
        ReadableService<I, R> {
}