package com.lsv.lib.spring.web.client.core;

import com.lsv.lib.core.event.EventPublisher;
import com.lsv.lib.spring.web.commons.event.WebClientRequestInterceptEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.AbstractReactorHttpExchangeAdapter;
import org.springframework.web.service.invoker.HttpRequestValues;
import org.springframework.web.service.invoker.ReactorHttpExchangeAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Intercepts all WebClient calls to raise events and allow them to be handled.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
public class InterceptorHttpClientAdapter extends AbstractReactorHttpExchangeAdapter {

    private final WebClientRequestInterceptEvent eventWebClientRequestIntercept;
    private final ReactorHttpExchangeAdapter delegate;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public InterceptorHttpClientAdapter(Class<?> clientService, String configurationId, WebClient webClient) {
        this.eventWebClientRequestIntercept = WebClientRequestInterceptEvent.builder()
            .configurationId(configurationId)
            .clientClass(clientService)
            .webClient(webClient)
            .build();
        this.delegate = WebClientAdapter.create(webClient);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public Mono<Void> exchangeForMono(HttpRequestValues requestValues) {
        return publish("exchangeForMono", requestValues, null,
            delegate.exchangeForMono(requestValues)
        );
    }

    @Override
    public Mono<HttpHeaders> exchangeForHeadersMono(HttpRequestValues requestValues) {
        return publish("exchangeForHeadersMono", requestValues, null,
            delegate.exchangeForHeadersMono(requestValues)
        );
    }

    @Override
    public <T> Mono<T> exchangeForBodyMono(HttpRequestValues requestValues, ParameterizedTypeReference<T> bodyType) {
        return publish("exchangeForBodyMono", requestValues, bodyType,
            delegate.exchangeForBodyMono(requestValues, bodyType)
        );
    }

    @Override
    public <T> Flux<T> exchangeForBodyFlux(HttpRequestValues requestValues, ParameterizedTypeReference<T> bodyType) {
        return publish("exchangeForBodyFlux", requestValues, bodyType,
            delegate.exchangeForBodyFlux(requestValues, bodyType)
        );
    }

    @Override
    public Mono<ResponseEntity<Void>> exchangeForBodilessEntityMono(HttpRequestValues requestValues) {
        return publish("exchangeForBodilessEntityMono", requestValues, null,
            delegate.exchangeForBodilessEntityMono(requestValues)
        );
    }

    @Override
    public <T> Mono<ResponseEntity<T>> exchangeForEntityMono(HttpRequestValues requestValues, ParameterizedTypeReference<T> bodyType) {
        return publish("exchangeForEntityMono", requestValues, bodyType,
            delegate.exchangeForEntityMono(requestValues, bodyType)
        );
    }

    @Override
    public <T> Mono<ResponseEntity<Flux<T>>> exchangeForEntityFlux(HttpRequestValues requestValues, ParameterizedTypeReference<T> bodyType) {
        return publish("exchangeForEntityFlux", requestValues, bodyType,
            delegate.exchangeForEntityFlux(requestValues, bodyType)
        );
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    private <T> T publish(String method, HttpRequestValues requestValues, ParameterizedTypeReference<?> bodyType, T result) {
        log.trace("intercept {}", method);

        var event = eventWebClientRequestIntercept.toBuilder()
            .requestValues(requestValues)
            .bodyType(bodyType)
            .resultMonoFlux(result)
            .build();

        EventPublisher.publish(event);

        // Returns the object that may have been modified
        return (T) event.resultMonoFlux();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public boolean supportsRequestAttributes() {
        return true;
    }
}