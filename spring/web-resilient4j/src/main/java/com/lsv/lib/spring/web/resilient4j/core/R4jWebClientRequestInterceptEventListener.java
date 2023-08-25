package com.lsv.lib.spring.web.resilient4j.core;

import com.lsv.lib.core.function.Resolver;
import com.lsv.lib.core.helper.HelperObj;
import com.lsv.lib.spring.web.commons.event.WebClientRequestInterceptEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.ApplicationListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

import static com.lsv.lib.core.helper.HelperLog.trace;

/**
 * Listener for the EventWebClientRequestIntercept event.<p>
 * Using a @Bean would solve it, but creating the class will make what is being done more readable.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
@RequiredArgsConstructor
public class R4jWebClientRequestInterceptEventListener implements ApplicationListener<WebClientRequestInterceptEvent> {

    private final Resolver<WebClientRequestInterceptEvent, Collection<R4jOperator<?>>> operatorResolverByEvent;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public void onApplicationEvent(WebClientRequestInterceptEvent event) {
        log(event);

        var r4jOperators = operatorResolverByEvent.resolve(event);
        trace(log, () -> "Configurações utilizadas: \n\n%s\n".formatted(HelperObj.toString(r4jOperators.stream().map(R4jOperator::r4jInstance))));

        r4jOperators.stream()
            .filter(ObjectUtils::isNotEmpty)
            .forEach(r4jOperator -> {
                // So the changes made can be used by whoever triggered the event
                event.resultMonoFlux(
                    transformDeferred(r4jOperator, event.resultMonoFlux()));
            });
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void log(WebClientRequestInterceptEvent event) {
        trace(log, () -> """
            Resilient4j event listener. Event:
                            
            Service: %s
            HttpMethod: %s,
            URI: %s
            """.formatted(
            event.clientClass().getName(),
            event.requestValues().getHttpMethod(),
            event.requestValues().getUriTemplate())
        );
    }

    @SuppressWarnings("all")
    private Object transformDeferred(R4jOperator<?> r4jOperator, Object result) {
        if (result instanceof Mono mono) {
            return mono.transformDeferred(r4jOperator);
        } else if (result instanceof Flux flux) {
            return flux.transformDeferred(r4jOperator);
        }
        return result;
    }
}
