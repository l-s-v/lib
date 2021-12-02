package com.lsv.lib.spring.jpa.helper;

import com.lsv.lib.core.concept.dto.Filter;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class HelperConverterSpringJpa {

    public static Pageable to(@NonNull Filter<?> filter) {
        return Optional.ofNullable(filter.page())
            .map(page -> PageRequest.of(page.numPage(), page.size(), to(filter.orderBies())))
            .orElse(null);
    }

    public static Sort to(List<Filter.OrderBy> orderBies) {
        return Optional.ofNullable(orderBies)
            .map(orderBIES -> Sort.by(orderBIES.stream()
                .map(HelperConverterSpringJpa::to).collect(Collectors.toList())))
            .orElse(Sort.unsorted());
    }

    public static Sort.Order to(@NonNull Filter.OrderBy orderBy) {
        return orderBy.asc()
            ? Sort.Order.asc(orderBy.property())
            : Sort.Order.desc(orderBy.property());
    }
}