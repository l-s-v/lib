package com.lsv.lib.core.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Leandro da Silva Vieira
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LibConstants {

    public static final String BASE_PROPERTIES = "lsv.";
    public static final String BASE_LIB_PROPERTIES = "lib.";
    public static final String BASE_PACKAGE = "com." + BASE_PROPERTIES + BASE_LIB_PROPERTIES;

    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String DEFAULT_PROPERTIES_FILE_NAME = "default.properties";
}