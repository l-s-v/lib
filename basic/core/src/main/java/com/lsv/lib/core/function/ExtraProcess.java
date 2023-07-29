package com.lsv.lib.core.function;

/**
 * Sets the default for extra processes that can be configured.
 *
 * @author Leandro da Silva Vieira
 */
@FunctionalInterface
public interface ExtraProcess<T>  {

    void process(T var1);
}