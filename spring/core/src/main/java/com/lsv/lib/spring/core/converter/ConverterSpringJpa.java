package com.lsv.lib.spring.core.converter;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.mapper.Mappable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConverterSpringJpa {

    public static Pageable to(@NonNull Filter<?> filter) {
        return Optional.ofNullable(filter.getPage())
                .map(page -> PageRequest.of(page.getNumPage(), page.getSize(), to(filter.getSorts())))
                .orElse(null);
    }

    public static Sort to(List<Filter.Sort> orderBies) {
        return Optional.ofNullable(orderBies)
                .map(orderBIES -> org.springframework.data.domain.Sort.by(orderBIES.stream()
                        .map(ConverterSpringJpa::to).toList()))
                .orElse(org.springframework.data.domain.Sort.unsorted());
    }

    public static Sort.Order to(@NonNull Filter.Sort orderBy) {
        return orderBy.isDesc()
                ? org.springframework.data.domain.Sort.Order.desc(orderBy.getProperty())
                : org.springframework.data.domain.Sort.Order.asc(orderBy.getProperty());
    }

    public static <I extends Serializable> Page<I> to(@NonNull ListDto<I> listaDto, Pageable pageable) {
        return new PageImpl<>(listaDto.getRecords(), pageable, listaDto.totalRecords());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    public static <T extends Identifiable<?>> Filter<T> of(@NonNull Pageable pageable) {
        return Filter.<T>of(null)
                .page(Filter.Page.of()
                        .numPage(pageable.getPageNumber())
                        .size(pageable.getPageSize())
                        .get())
                .sorts(of(pageable.getSort()))
                .get();
    }

    public static List<Filter.Sort> of(@NonNull Sort sort) {
        return sort.get().map(ConverterSpringJpa::of).toList();
    }

    public static Filter.Sort of(@NonNull Sort.Order order) {
        return Filter.Sort.of()
                .desc(order.isDescending())
                .property(order.getProperty())
                .get();
    }

    public static <I extends Serializable, P> ListDto<I> of(@NonNull Page<P> page, Mappable<I, P> mappable) {
        return ListDto
                .of(mappable.of(page.getContent()))
                .totalRecords(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .get();
    }
}
