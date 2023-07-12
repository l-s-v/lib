package com.lsv.lib.core.mapper;

/**
 * Sets the factory default for mapping between objects.
 *
 * @author Leandro da Silva Vieira
 */
public interface MappableFactory {

    <S, D> Mappable<S, D> create(Class<S> sourceClass, Class<D> destinationClass);
}