package com.lsv.lib.core.helper;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Some utilities for working with functions.
 *
 * @author Leandro da Silva Vieira
 */
public class HelperFunction {

    public static void run(boolean condition, Runnable runnable) {
        if (condition) {
            runnable.run();
        }
    }

    public static <T> UnaryOperator<T> peek(Consumer<T> c) {
        return x -> {
            c.accept(x);
            return x;
        };
    }
}