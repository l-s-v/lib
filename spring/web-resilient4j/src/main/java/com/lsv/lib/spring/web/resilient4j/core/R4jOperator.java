package com.lsv.lib.spring.web.resilient4j.core;

import com.lsv.lib.spring.web.resilient4j.properties.R4jInstance;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.reactivestreams.Publisher;

import java.util.function.UnaryOperator;

/**
 * Lets you group information from all Resilient4J Operator objects under a single type.
 *
 * @author Leandro da Silva Vieira
 */
@RequiredArgsConstructor
@ToString(of = "r4jInstance")
@Getter
public class R4jOperator<T> implements UnaryOperator<Publisher<T>> {

    private final UnaryOperator<Publisher<T>> source;
    private final R4jInstance r4jInstance;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public Publisher<T> apply(Publisher<T> tPublisher) {
        return source.apply(tPublisher);
    }
}