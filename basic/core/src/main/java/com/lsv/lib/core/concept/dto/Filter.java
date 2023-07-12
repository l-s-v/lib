package com.lsv.lib.core.concept.dto;

import com.lsv.lib.core.behavior.Identifiable;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.reflections.util.FilterBuilder;

import java.util.List;
import java.util.Map;

/**
 * @author Leandro da Silva Vieira
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(buildMethodName = "get")
@ToString
public class Filter<T extends Identifiable<?>> implements Dto {

    private T obj;
    private boolean loadQuantityRecords;
    private boolean onlyQuantityRecords;
    private boolean noCache;
    private Page page;
    @Singular
    private List<OrderBy> orderBies;
    @Singular
    private Map<String, Object> extraParameters;

    @ToString
    @Getter
    @Builder(builderMethodName = "of", buildMethodName = "get")
    public static class Page {
        int numPage;
        int size;
    }

    @ToString
    @Getter
    @Builder(builderMethodName = "of", buildMethodName = "get")
    public static class OrderBy {
        @NonNull String property;
        boolean asc;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("rawtypes")
    public static <V extends Identifiable<?>> FilterBuilder of(V obj) {
        return Filter.builder().obj(obj);
    }
}