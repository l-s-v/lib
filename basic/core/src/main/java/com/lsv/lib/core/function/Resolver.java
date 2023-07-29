package com.lsv.lib.core.function;

/**
 * Sets the default for one-way resolver between objects.
 *
 * @author Leandro da Silva Vieira
 */
@FunctionalInterface
public interface Resolver<S, D>  {

    D resolve(S source);
}