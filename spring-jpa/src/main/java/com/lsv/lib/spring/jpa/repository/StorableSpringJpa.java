package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface StorableSpringJpa
    <
        P extends Persistable<ID>,
        ID extends Serializable>
    extends
    Storable<P, ID>,
    JpaRepository <P, ID>,
    JpaSpecificationExecutor<P> {
}