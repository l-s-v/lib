package com.lsv.lib.core.concept.dto;

import com.lsv.lib.core.behavior.Identifiable;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "get")
public class Filter<T extends Identifiable<?>> implements Dto {

    @NonNull
    private T obj;
    @Builder.Default
    private Long positionFirstRecord = 0L;
    @Builder.Default
    private Long maximumNumberOfRecords = 0L;
    private boolean loadQuantityRecords;
    private boolean onlyQuantityRecords;
    private boolean noCache;
    @Singular
    private List<OrderBy> orderBys;
    @Singular
    private Map<String, Object> extraParameters;

    public record OrderBy(@NonNull String atributo, boolean ascendente) {
    }

    // Substituindo a criação do Builder para a tipagem ser automática
    public static <V extends Identifiable<?>> FilterBuilder<V> of(V obj) {
        return new FilterBuilder<V>().obj(obj);
    }
}