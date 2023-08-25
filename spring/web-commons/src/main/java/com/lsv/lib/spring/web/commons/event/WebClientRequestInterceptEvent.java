package com.lsv.lib.spring.web.commons.event;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.invoker.HttpRequestValues;

import java.util.function.Supplier;

/**
 * Event created to allow integration between spring-web-client and spring-web-resilient4j modules without any direct dependencies.
 *
 * @author Leandro da Silva Vieira
 */
@Getter
@Builder(toBuilder = true)
public class WebClientRequestInterceptEvent extends ApplicationEvent {

    @NotNull
    private String configurationId;
    @NotNull
    private WebClient webClient;
    @NotNull
    private Class<?> clientClass;
    @NotNull
    private HttpRequestValues requestValues;
    private ParameterizedTypeReference<?> bodyType;
    /**
     * This object is very important, as it allows the response (mono or flux, Supplier content) to be changed
     * by the event listener, before it continues the result.
     * To ensure this, the event needs to be synchronous.
     */
    @NotNull
    @Setter
    private Object resultMonoFlux;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public WebClientRequestInterceptEvent(String configurationId, WebClient webClient, Class<?> clientClass, HttpRequestValues requestValues, ParameterizedTypeReference<?> bodyType, Object resultMonoFlux) {
        this();
        this.configurationId = configurationId;
        this.webClient = webClient;
        this.clientClass = clientClass;
        this.requestValues = requestValues;
        this.bodyType = bodyType;
        this.resultMonoFlux = resultMonoFlux;
    }

    public WebClientRequestInterceptEvent() {
        super(StringUtils.EMPTY);
    }
}