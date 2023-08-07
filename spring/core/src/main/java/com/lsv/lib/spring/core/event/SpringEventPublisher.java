package com.lsv.lib.spring.core.event;

import com.lsv.lib.core.event.EventPublisher;
import com.lsv.lib.spring.core.loader.SpringLoader;
import org.springframework.stereotype.Component;

/**
 * Provides an event publishing implementation for Spring.
 *
 * @author Leandro da Silva Vieira
 */
@Component
public class SpringEventPublisher implements EventPublisher {

    @Override
    public void publishEvent(Object source) {
        SpringLoader.applicationContext().publishEvent(source);
    }
}