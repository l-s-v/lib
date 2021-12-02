package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mapper;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.spring.jpa.helper.HelperConverterSpringJpa;
import lombok.NonNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface ReadableRepository<T extends Identifiable<ID>, ID extends Serializable, P extends Persistable<?>> extends Repository<T>, Readable<T> {

    default Optional<T> findById(@NonNull T objIdentifiable) {
        return this.jpaRepository().findById(objIdentifiable.id())
            .map(p -> mapper().of(p));
    }

    default ListDto<T> findByFilter(@NonNull Filter<T> filter) {
        Pageable pageable = HelperConverterSpringJpa.to(filter);

        Example<P> example = null;
        if (filter.obj() != null) {
            example = Example.of(mapper().to(filter.obj()));
        }

        if (pageable != null) {
            return this.findAll(example, pageable);
        } else {
            return this.findAll(example, HelperConverterSpringJpa.to(filter.orderBies()));
        }
    }

    default ListDto<T> findAll(Example<P> example, @NonNull Pageable pageable) {
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

    default ListDto<T> findAll(Example<P> example, @NonNull Sort sort) {
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

    Mapper<T, P> mapper();
    JpaRepository<P, ID> jpaRepository();
}