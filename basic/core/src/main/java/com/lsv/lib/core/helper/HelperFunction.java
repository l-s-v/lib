package com.lsv.lib.core.helper;

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
}