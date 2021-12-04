package com.lsv.lib.core.concept.dto;

import com.lsv.lib.core.behavior.Identifiable;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "get")
public class Filter<T extends Identifiable<?>> implements Dto {

    @With
    private T obj;
    private boolean loadQuantityRecords;
    private boolean onlyQuantityRecords;
    private boolean noCache;
    private Page page;
    @Singular
    private List<OrderBy> orderBies;
    @Singular
    private Map<String, Object> extraParameters;

    @Builder
    @Getter
    public static class Page {
        int numPage;
        int size;
        OrderBy orderBy;
    }

    @Builder
    @Getter
    public static class OrderBy {
        @NonNull String property;
        boolean asc;
    }

    // Substituindo a criação do Builder para a tipagem ser automática
    public static <V extends Identifiable<?>> FilterBuilder<V> of(V obj) {
        return new FilterBuilder<V>().obj(obj);
    }
}