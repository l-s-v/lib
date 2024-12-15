package com.lsv.lib.core.concept.dto;

import com.lsv.lib.core.helper.HelperObj;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author Leandro da Silva Vieira
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(buildMethodName = "get")
@ToString
public class Filter<T extends Serializable> implements Dto {

    public static final String ASC = "asc";
    public static final String DESC = "desc";
    public static final List<String> DIRECTIONS = List.of(ASC, DESC);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private T obj;
    private Page page;
    @Singular
    private List<Sort> sorts;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Shortcut that allows you to set multiple OrderBys in the form of comma-separated text.
     */
    public void setSort(String sort) {
        var ordersBy = new LinkedList<Sort>();

        Optional
            .ofNullable(sort)
            .map(s -> s.split(","))
            .map(List::of).stream()
            .flatMap(Collection::stream)
            .forEach(sortTemp -> {
                if (DIRECTIONS.contains(sortTemp) && !ordersBy.isEmpty()) {
                    ordersBy.getLast().setDesc(DESC.equalsIgnoreCase(sortTemp));
                } else {
                    ordersBy.add(Sort.of().property(sortTemp).get());
                }
            });

        setSorts(ordersBy);
    }

    /**
     * Allows you to confirm the object's class by performing a conversion if it is a json string.
     */
    public T getObj(Class<T> type) {
        if (ObjectUtils.isNotEmpty(this.getObj()) && this.getObj() instanceof String) {
            this.setObj(HelperObj.readValue(String.valueOf(this.getObj()), type));
        }
        return this.getObj();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(builderMethodName = "of", buildMethodName = "get")
    public static class Page implements Dto {
        int numPage;
        int size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(builderMethodName = "of", buildMethodName = "get")
    public static class Sort implements Dto {
        @NotNull
        String property;
        boolean desc;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("rawtypes")
    public static <V extends Serializable> FilterBuilder of(V obj) {
        return Filter.builder().obj(obj);
    }
}
