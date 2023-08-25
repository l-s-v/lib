package com.lsv.lib.spring.web.resilient4j.properties;

/**
 * Resilience4J resource types.
 *
 * @author Leandro da Silva Vieira
 */
public enum R4jType {
    CIRCUITE_BREAKER,
    BULKHEAD,
    RETRY,
    RATE_LIMITER,
    TIME_LIMITER;
}