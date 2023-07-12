package com.lsv.lib.core.concept.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Delegate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Leandro da Silva Vieira
 */
@Getter
@Builder(builderMethodName = "of", buildMethodName = "get")
public class ListDto<T> implements List<T>, Dto {

    @NonNull
    @Delegate
    private List<T> records;
    @Setter
    private Long totalRecords;
    @Setter
    private Integer totalPages;

    public Long totalRecords() {
        return Optional.ofNullable(totalRecords).orElseGet(() -> (long) records().size());
    }

    // Substituindo a criação do Builder para a tipagem ser automática
    public static <V> ListDtoBuilder<V> of(List<V> records) {
        return new ListDtoBuilder<V>().records(records);
    }

    public static <T> ListDto<T> empty() {
        return ListDto.of(Collections.<T>emptyList()).get();
    }
}