package com.lsv.lib.core.behavior;

import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Define o comportamento de mapeadores de objetos.
 *
 * @author Leandro da Silva Vieira
 */
public interface Mapper<S, D> {

    Mapper<S, D> setup(Class<S> sourceClass, Class<D> destinationClass);
    D to(S source);
    S of(D destination);

    default List<D> to(@NonNull List<S> sources) {
        return sources.stream().map(this::to).collect(Collectors.toList());
    }

    default List<S> of(@NonNull List<D> destinations) {
        return destinations.stream().map(this::of).collect(Collectors.toList());
    }
}