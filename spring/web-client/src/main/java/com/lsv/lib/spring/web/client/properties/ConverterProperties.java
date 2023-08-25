package com.lsv.lib.spring.web.client.properties;

import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * @author Leandro da Silva Vieira
 */
public interface ConverterProperties<T> extends Converter<String, T> {

    @SuppressWarnings("unchecked")
    default T newInstance(String className) throws Exception {
        return (T) Class.forName(className).getConstructor().newInstance();
    }

    default T handlerError(Exception exception, String className, String msgExpected) {
        try {
            throw exception;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("A classe %s não foi encontrada".formatted(className));
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("A classe %s deve ser uma %s".formatted(className, msgExpected));
        } catch (Throwable e) {
            throw new IllegalArgumentException("Não foi possível instanciar a classe %s".formatted(className));
        }
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Component
    @ConfigurationPropertiesBinding
    class ConverterSupplierExchangeFilterFunction implements ConverterProperties<Supplier<ExchangeFilterFunction>> {

        @Override
        public Supplier<ExchangeFilterFunction> convert(@NonNull String className) {
            try {
                return newInstance(className);
            } catch (Exception e) {
                return handlerError(e, className, "Supplier<ExchangeFilterFunction>");
            }
        }
    }

    @Component
    @ConfigurationPropertiesBinding
    class ConverterUnaryOperatorWebClientBuilder implements ConverterProperties<UnaryOperator<WebClient.Builder>> {

        @Override
        public UnaryOperator<WebClient.Builder> convert(@NonNull String className) {
            try {
                return newInstance(className);
            } catch (Exception e) {
                return handlerError(e, className, "UnaryOperator<WebClient.Builder>");
            }
        }
    }

    @Component
    @ConfigurationPropertiesBinding
    class ConverterUnaryOperatorHttpServiceProxyFactory implements ConverterProperties<UnaryOperator<HttpServiceProxyFactory>> {

        @Override
        public UnaryOperator<HttpServiceProxyFactory> convert(@NonNull String className) {
            try {
                return newInstance(className);
            } catch (Exception e) {
                return handlerError(e, className, "UnaryOperator<HttpServiceProxyFactory>");
            }
        }
    }
}