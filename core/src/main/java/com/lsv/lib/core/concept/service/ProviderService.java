package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.helper.ProviderServiceImpl;

public interface ProviderService<I extends Identifiable<?>, R extends Repository<I>> {

    default R repository() {
        return ProviderServiceImpl.repository(this);
    }

    default ProviderService<I, R> repository(R repository) {
        ProviderServiceImpl.repository(this, repository);
        return this;
    }
}