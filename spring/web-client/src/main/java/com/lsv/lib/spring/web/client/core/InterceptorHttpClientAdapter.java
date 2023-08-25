package com.lsv.lib.spring.web.client.core;

import com.lsv.lib.core.event.EventPublisher;
import com.lsv.lib.spring.web.commons.event.WebClientRequestInterceptEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpClientAdapter;
import org.springframework.web.service.invoker.HttpRequestValues;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Intercepts all WebClient calls to raise events and allow them to be handled.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
public class InterceptorHttpClientAdapter implements HttpClientAdapter {

    private final WebClientRequestInterceptEvent eventWebClientRequestIntercept;
    private final HttpClientAdapter delegate;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public InterceptorHttpClientAdapter(Class<?> clientService, String configurationId, WebClient webClient) {
        this.eventWebClientRequestIntercept = WebClientRequestInterceptEvent.builder()
            .configurationId(configurationId)
            .clientClass(clientService)
            .webClient(webClient)
            .build();
        this.delegate = WebClientAdapter.forClient(webClient);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public Mono<Void> requestToVoid(HttpRequestValues requestValues) {
        return publish("requestToVoid", requestValues, null,
            delegate.requestToVoid(requestValues)
        );
    }

    @Override
    public Mono<HttpHeaders> requestToHeaders(HttpRequestValues requestValues) {
        return publish("requestToHeaders", requestValues, null,
            delegate.requestToHeaders(requestValues)
        );
    }

    @Override
    public <T> Mono<T> requestToBody(HttpRequestValues requestValues, ParameterizedTypeReference<T> bodyType) {
        return publish("requestToBody", requestValues, bodyType,
            delegate.requestToBody(requestValues, bodyType)
        );
    }

    @Override
    public <T> Flux<T> requestToBodyFlux(HttpRequestValues requestValues, ParameterizedTypeReference<T> bodyType) {
        return publish("requestToBodyFlux", requestValues, bodyType,
            delegate.requestToBodyFlux(requestValues, bodyType)
        );
    }

    @Override
    public Mono<ResponseEntity<Void>> requestToBodilessEntity(HttpRequestValues requestValues) {
        return publish("requestToBodilessEntity", requestValues, null,
            delegate.requestToBodilessEntity(requestValues)
        );
    }

    @Override
    public <T> Mono<ResponseEntity<T>> requestToEntity(HttpRequestValues requestValues, ParameterizedTypeReference<T> bodyType) {
        return publish("requestToEntity", requestValues, bodyType,
            delegate.requestToEntity(requestValues, bodyType)
        );
    }

    @Override
    public <T> Mono<ResponseEntity<Flux<T>>> requestToEntityFlux(HttpRequestValues requestValues, ParameterizedTypeReference<T> bodyType) {
        return publish("requestToEntityFlux", requestValues, bodyType,
            delegate.requestToEntityFlux(requestValues, bodyType)
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
}