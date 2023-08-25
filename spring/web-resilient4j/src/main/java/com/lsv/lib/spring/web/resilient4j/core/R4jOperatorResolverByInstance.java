package com.lsv.lib.spring.web.resilient4j.core;

import com.lsv.lib.core.function.Resolver;
import com.lsv.lib.core.helper.HelperObj;
import com.lsv.lib.spring.web.resilient4j.properties.R4jInstance;
import com.lsv.lib.spring.web.resilient4j.properties.R4jType;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.reactor.bulkhead.operator.BulkheadOperator;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.reactor.timelimiter.TimeLimiterOperator;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static com.lsv.lib.core.helper.HelperLog.trace;

/**
 * Stores all Resilience4J settings in a concentrated way.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
public class R4jOperatorResolverByInstance implements Resolver<R4jInstance, R4jOperator<?>> {

    private static final String R4J_DEFAULT = "R4J_DEFAULT";

    private final Map<R4jType, Map<String, R4jOperator<?>>> operators = new HashMap<>();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public R4jOperatorResolverByInstance(CircuitBreakerRegistry circuitBreakerRegistry,
                                         BulkheadRegistry bulkheadRegistry,
                                         RetryRegistry retryRegistry,
                                         RateLimiterRegistry rateLimiterRegistry,
                                         TimeLimiterRegistry timeLimiterRegistry) {
        initialize(
            circuitBreakerRegistry,
            bulkheadRegistry,
            retryRegistry,
            rateLimiterRegistry,
            timeLimiterRegistry
        );
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public R4jOperator<?> resolve(R4jInstance r4jInstance) {
        return operators.get(r4jInstance.getType())
            .get(ObjectUtils.defaultIfNull(r4jInstance.getName(), R4J_DEFAULT));
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void initialize(CircuitBreakerRegistry circuitBreakerRegistry,
                            BulkheadRegistry bulkheadRegistry,
                            RetryRegistry retryRegistry,
                            RateLimiterRegistry rateLimiterRegistry,
                            TimeLimiterRegistry timeLimiterRegistry) {

        // All will have at least one default configuration
        circuitBreakerRegistry.circuitBreaker(R4J_DEFAULT);
        bulkheadRegistry.bulkhead(R4J_DEFAULT);
        retryRegistry.retry(R4J_DEFAULT);
        rateLimiterRegistry.rateLimiter(R4J_DEFAULT);
        timeLimiterRegistry.timeLimiter(R4J_DEFAULT);

        // @formatter:off
        addOperators(createOperators(
            circuitBreakerRegistry.getAllCircuitBreakers(),
            circuitBreaker -> createR4jOperator(
                R4jType.CIRCUITE_BREAKER,
                circuitBreaker.getName(),
                CircuitBreakerOperator.of(circuitBreaker))))
        .addOperators(createOperators(
            bulkheadRegistry.getAllBulkheads(),
            bulkhead -> createR4jOperator(
                R4jType.BULKHEAD,
                bulkhead.getName(),
                BulkheadOperator.of(bulkhead))))
        .addOperators(createOperators(
            retryRegistry.getAllRetries(),
            retry -> createR4jOperator(
                R4jType.RETRY,
                retry.getName(),
                RetryOperator.of(retry))))
        .addOperators(createOperators(
            rateLimiterRegistry.getAllRateLimiters(),
            rateLimiter -> createR4jOperator(
                R4jType.RATE_LIMITER,
                rateLimiter.getName(),
                RateLimiterOperator.of(rateLimiter))))
        .addOperators(createOperators(
            timeLimiterRegistry.getAllTimeLimiters(),
            timeLimiter -> createR4jOperator(
                R4jType.TIME_LIMITER,
                timeLimiter.getName(),
                TimeLimiterOperator.of(timeLimiter))));
        // @formatter:on

        trace(log, () -> "initialize R4JOperators = %s".formatted(HelperObj.toString(operators)));
    }

    private <T, R extends R4jOperator<?>> Map<String, R> createOperators(Set<T> instances,
                                                                         Function<? super T, Map<String, R>> mapper) {
        return instances.stream()
            .map(mapper)
            .flatMap(map -> map.entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @SuppressWarnings("all")
    private <T> Map<String, R4jOperator<?>> createR4jOperator(R4jType r4jType, String name, T operator) {
        return Map.of(name,
            new R4jOperator((UnaryOperator<Publisher<?>>) operator,
                new R4jInstance()
                    .setType(r4jType)
                    .setName(name)));
    }

    private R4jOperatorResolverByInstance addOperators(Map<String, R4jOperator<?>> value) {
        operators.put(value.values().stream().findFirst().get().r4jInstance().getType(), value);
        return this;
    }
}