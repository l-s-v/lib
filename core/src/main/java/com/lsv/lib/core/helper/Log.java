package com.lsv.lib.core.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log {

    public static Logger of(Object source) {
        return LoggerFactory.getLogger(source.getClass());
    }
}