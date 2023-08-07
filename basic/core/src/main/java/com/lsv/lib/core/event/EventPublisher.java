package com.lsv.lib.core.event;

import com.lsv.lib.core.loader.Loader;

import java.util.List;

/**
 * Sets the default for event publishers.
 *
 * @author Leandro da Silva Vieira
 */
public interface EventPublisher {

    /**
     * Must publish the object as an event.
     */
    void publishEvent(Object source);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    List<EventPublisher> PUBLISHERS = Loader.of(EventPublisher.class).findImplementationsByAllLoaders();

    /**
     * Publishes the event to all found event publishers.
     */
    static void publish(Object source) {
        PUBLISHERS.forEach(eventPublisher -> eventPublisher.publishEvent(source));
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
}