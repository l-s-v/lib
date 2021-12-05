package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.concept.repository.ReadableRepository;
import com.lsv.lib.spring.jpa.helper.ConverterSpringJpa;
import lombok.NonNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface ReadableRepositorySpringJpa<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S extends StorableSpringJpa<P, ID>>
    extends
    ReadableRepository<I, ID, P, S>,
    RepositoryImplementeableSpringJpa<I, ID, P, S> {

    default ListDto<I> findByFilter(@NonNull Filter<I> filter) {
        Pageable pageable = ConverterSpringJpa.to(filter);
        Specification<P> specification = this.specificationToFindByFilter();
        Example<P> example = null;

        if (specification == null && filter.obj() != null) {
            example = Example.of(providerRepository().mappable().to(filter.obj()));
        }

        if (pageable != null) {
            return this.findAllPageable(example, specification, pageable);
        } else {
            return this.findAllSorted(example, specification, ConverterSpringJpa.to(filter.orderBies()));
        }
    }

    default Specification<P> specificationToFindByFilter() {
        return null;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private ListDto<I> findAllPageable(Example<P> example,
                                       Specification<P> specification,
                                       @NonNull Pageable pageable) {

        Page<P> page;

        if (specification != null) {
            page = providerRepository().storable().findAll(specification, pageable);
        } else if (example != null) {
            page = providerRepository().storable().findAll(example, pageable);
        } else {
            page = providerRepository().storable().findAll(pageable);
        }

        return Optional.of(page)
            .map(ps -> ListDto
                .of(providerRepository().mappable().of(ps.getContent()))
                .totalRecords(ps.getTotalElements())
                .get())
            .orElse(ListDto.empty());
    }

    private ListDto<I> findAllSorted(Example<P> example,
                                     Specification<P> specification,
                                     @NonNull Sort sort) {

        List<P> results;

        if (specification != null) {
            results = providerRepository().storable().findAll(specification, sort);
        } else if (example != null) {
            results = providerRepository().storable().findAll(example, sort);
        } else {
            results = providerRepository().storable().findAll(sort);
        }

        return Optional.of(results)
            .map(ps -> ListDto
                .of(providerRepository().mappable().of(ps))
                .get())
            .orElse(ListDto.empty());
    }
}