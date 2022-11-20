package com.lsv.lib.spring.core;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import lombok.NonNull;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

public final class ConverterSpringJpa {

    public static Pageable to(@NonNull Filter<?> filter) {
        return Optional.ofNullable(filter.page())
                .map(page -> PageRequest.of(page.numPage(), page.size(), to(filter.orderBies())))
                .orElse(null);
    }

    public static Sort to(List<Filter.OrderBy> orderBies) {
        return Optional.ofNullable(orderBies)
                .map(orderBIES -> Sort.by(orderBIES.stream()
                        .map(ConverterSpringJpa::to).toList()))
                .orElse(Sort.unsorted());
    }

    public static Sort.Order to(@NonNull Filter.OrderBy orderBy) {
        return orderBy.asc()
                ? Sort.Order.asc(orderBy.property())
                : Sort.Order.desc(orderBy.property());
    }

    public static <I> Page<I> to(@NonNull ListDto<I> listaDto, Pageable pageable) {
        return new PageImpl<>(listaDto.records(), pageable, listaDto.totalRecords());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    public static <T extends Identifiable<?>> Filter<T> of(@NonNull Pageable pageable) {
        return Filter.<T>of(null)
                .page(Filter.Page.of()
                        .numPage(pageable.getPageNumber())
                        .size(pageable.getPageSize())
                        .get())
                .orderBies(of(pageable.getSort()))
                .get();
    }

    public static List<Filter.OrderBy> of(@NonNull Sort sort) {
        return sort.get().map(ConverterSpringJpa::of).toList();
    }

    public static Filter.OrderBy of(@NonNull Sort.Order order) {
        return Filter.OrderBy.of()
                .asc(order.isAscending())
                .property(order.getProperty())
                .get();
    }

    public static <I, P> ListDto<I> of(@NonNull Page<P> page, Mappable<I, P> mappable) {
        return ListDto
                .of(mappable.of(page.getContent()))
                .totalRecords(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .get();
    }
}