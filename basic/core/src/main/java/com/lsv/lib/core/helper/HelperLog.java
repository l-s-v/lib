package com.lsv.lib.core.helper;

import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.function.Supplier;

/**
 * Allows executing the entire block of code only if the condition is met.
 * This is so as not to waste all possible treatment of parameters for situations where the log will not be used.
 *
 * @author Leandro da Silva Vieira
 */
public final class HelperLog {

    public static void trace(Logger log, Supplier<String> msg) {
        log(log, Level.TRACE, msg);
    }

    public static void debug(Logger log, Supplier<String> msg) {
        log(log, Level.DEBUG, msg);
    }

    public static void info(Logger log, Supplier<String> msg) {
        log(log, Level.INFO, msg);
    }

    public static void warn(Logger log, Supplier<String> msg) {
        log(log, Level.WARN, msg);
    }

    public static void error(Logger log, Supplier<String> msg) {
        log(log, Level.ERROR, msg);
    }

    public static <T> void log(Logger log, Level level, Supplier<String> msg) {
        if (log.isEnabledForLevel(level)) {
            log.atLevel(level).log(msg);
        }
    }
}