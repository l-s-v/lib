package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.spring.jpa.helper.ConverterSpringJpa;
import lombok.NonNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface ReadableRepository<I extends Identifiable<ID>, ID extends Serializable, P extends Persistable<?>>
    extends Repository<I>, Readable<I>, ProviderRepository<I, ID, P> {

    default Optional<I> findById(@NonNull I objIdentifiable) {
        return this.jpaRepository().findById(objIdentifiable.getId())
            .map(p -> mapper().of(p));
    }

    default ListDto<I> findByFilter(@NonNull Filter<I> filter) {
        Pageable pageable = ConverterSpringJpa.to(filter);

        Example<P> example = null;
        if (filter.obj() != null) {
            example = Example.of(mapper().to(filter.obj()));
        }

        if (pageable != null) {
            return this.findAll(example, pageable);
        } else {
            return this.findAll(example, ConverterSpringJpa.to(filter.orderBies()));
        }
    }

    default ListDto<I> findAll(Example<P> example, @NonNull Pageable pageable) {
        Page<P> page;

        if (example == null) {
            page = this.jpaRepository().findAll(pageable);
        } else {
            page = this.jpaRepository().findAll(example, pageable);
        }

        return Optional.of(page)
            .map(ps -> ListDto
                .of(mapper().of(ps.getContent()))
                .totalRecords(ps.getTotalElements())
                .get())
            .orElse(ListDto.empty());
    }

    default ListDto<I> findAll(Example<P> example, @NonNull Sort sort) {
        List<P> results;

        if (example == null) {
            results = this.jpaRepository().findAll(sort);
        } else {
            results = this.jpaRepository().findAll(example, sort);
        }

        return Optional.of(results)
            .map(ps -> ListDto
                .of(mapper().of(ps))
                .get())
            .orElse(ListDto.empty());
    }
}