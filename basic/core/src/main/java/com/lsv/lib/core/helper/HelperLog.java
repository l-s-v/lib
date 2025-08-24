package com.lsv.lib.core.helper;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Allows executing the entire block of code only if the condition is met.
 * This is so as not to waste all possible treatment of parameters for situations where the log will not be used.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
public final class HelperLog {

    public static void trace(Logger log, String msg, Supplier<?>... args) {
        log(log, Level.TRACE, msg, args);
    }

    public static void debug(Logger log, String msg, Supplier<?> ... args) {
        log(log, Level.DEBUG, msg, args);
    }

    public static void info(Logger log, String msg, Supplier<?> ... args) {
        log(log, Level.INFO, msg, args);
    }

    public static void warn(Logger log, String msg, Supplier<?> ... args) {
        log(log, Level.WARN, msg, args);
    }

    public static void error(Logger log, String msg, Supplier<?> ... args) {
        log(log, Level.ERROR, msg, args);
    }

    public static void log(Logger log, Level level, String msg, Supplier<?> ... args) {
        if (log.isEnabledForLevel(level)) {
            var loggingEventBuilder = log.atLevel(level);

            Stream.ofNullable(args)
                .map(List::of)
                .flatMap(Collection::stream)
                .forEach(loggingEventBuilder::addArgument);

            loggingEventBuilder.log(msg);
        }
    }
}