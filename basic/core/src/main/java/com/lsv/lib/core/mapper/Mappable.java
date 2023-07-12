package com.lsv.lib.core.mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Defines the pattern for mapping between objects.
 *
 * @author Leandro da Silva Vieira
 */
public interface Mappable<S, D> {

    D to(S source);

    S of(D destination);

    default List<D> to(List<S> sources) {
        return sources == null ? null : sources.stream().map(this::to).collect(Collectors.toList());
    }

    default List<S> of(List<D> destinations) {
        return destinations == null ? null : destinations.stream().map(this::of).collect(Collectors.toList());
    }
}