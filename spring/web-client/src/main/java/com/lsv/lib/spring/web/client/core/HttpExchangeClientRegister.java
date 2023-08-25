package com.lsv.lib.spring.web.client.core;

import com.lsv.lib.core.helper.HelperBeanValidation;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.spring.core.loader.SpringLoader;
import com.lsv.lib.spring.web.client.annotation.HttpExchangeClient;
import com.lsv.lib.spring.web.client.properties.WebClientModuleProperties;
import com.lsv.lib.spring.web.client.properties.WebClientProperties;
import com.lsv.lib.spring.web.commons.helper.WebSpringHelper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceArgumentResolver;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.LinkedList;
import java.util.stream.Collectors;

import static com.lsv.lib.spring.core.helper.ConstantsSpring.BASE_PACKAGE_SPRING;

/**
 * Looks for all interfaces with the @HttpExchangeClient annotation within the configured packages
 * and prepares the objects so that Spring can autowire them.
 *
 * @author Leandro da Silva Vieira
 * @see <a href="https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface">rest-http-interface</a>
 */
@Slf4j
public class HttpExchangeClientRegister {

    private final WebClientFactory webClientFactory;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public HttpExchangeClientRegister(WebClientModuleProperties webClientProperties,
                                      WebClientFactory webClientFactory) {


        this.webClientFactory = webClientFactory;

        startRegisterHttpExchangeClients(webClientProperties);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    protected void startRegisterHttpExchangeClients(WebClientModuleProperties webClientModuleProperties) {
        var packs = new LinkedList<String>();
        packs.add(BASE_PACKAGE_SPRING);
        packs.addAll(webClientModuleProperties.getBasePackages());

        var scanner = createScanner();

        packs.stream()
            .filter(ObjectUtils::isNotEmpty)
            .flatMap(pack ->
                scanner.findCandidateComponents(pack).stream()
                    .filter(beanDefinition -> beanDefinition instanceof AnnotatedBeanDefinition)
            )
            .collect(Collectors.toSet())
            .forEach(beanDefinition -> registerHttpExchangeClient((AnnotatedBeanDefinition) beanDefinition));
    }

    protected void registerHttpExchangeClient(AnnotatedBeanDefinition beanDefinition) {
        String configurationId = null;
        var attributes = beanDefinition.getMetadata().getAnnotationAttributes(HttpExchangeClient.class.getCanonicalName());
        if (ObjectUtils.isNotEmpty(attributes)) {
            configurationId = (String) attributes.get(HttpExchangeClient.ATTR_CONFIGURATION_ID);
        }

        log.trace("Registrando bean para a interface {} anotada com @{}", beanDefinition.getBeanClassName(), HttpExchangeClient.class.getSimpleName());

        var objClass = HelperClass.classForName(beanDefinition.getBeanClassName());
        var webClientProperties = webClientFactory.resolveWebClientProperties(configurationId);

        var httpServiceProxyFactory = customize(webClientProperties,
            createHttpServiceProxyFactory(objClass, configurationId,
                webClientFactory.createWebClient(configurationId, webClientProperties)
            ));

        SpringLoader.registerBean(objClass, httpServiceProxyFactory.createClient(objClass));
    }

    /**
     * @see HttpServiceProxyFactory
     * @see WebClientAdapter
     * @see org.springframework.web.service.invoker.HttpServiceProxyFactory.Builder#customArgumentResolver(HttpServiceArgumentResolver)
     */
    public HttpServiceProxyFactory createHttpServiceProxyFactory(Class<?> clientService, String configurationId, WebClient webClient) {
        return HttpServiceProxyFactory
            .builder(new InterceptorHttpClientAdapter(clientService, configurationId, webClient))
            .customArgumentResolver(objToMultiValueMapHttpServiceArgumentResolver())
            .build();
    }

    public HttpServiceProxyFactory customize(WebClientProperties webClientProperties, HttpServiceProxyFactory httpServiceProxyFactory) {
        return webClientProperties.getHttpServiceProxyFactoryCustomize() != null
            ? webClientProperties.getHttpServiceProxyFactoryCustomize().apply(httpServiceProxyFactory)
            : httpServiceProxyFactory;
    }

    /**
     * Configures the scanner to search for interfaces annotated with @HttpExchangeClient.
     */
    protected ClassPathScanningCandidateComponentProvider createScanner() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isInterface() &&
                    beanDefinition.getMetadata().isIndependent();
            }
        };
        provider.addIncludeFilter(new AnnotationTypeFilter(HttpExchangeClient.class));

        return provider;
    }

    /**
     * Automatically converts a parameter (POJO) to a MultiValueMap<String, Object> when:
     * <ul>
     * <li> parameter is not null
     * <li> parameter use @RequestBody annotation
     * <li> content-type of the for application/x-www-form-urlencoded method
     * <li> parameter is not of type MultiValueMap
     * <li> the parameter type is not one of the reactive types recognized by ReactiveAdapterRegistry
     * </ul>
     * If use the @Valid annotation, also performs validation on the original object before converting. <p>
     * First, it uses the registered ObjectMapper to convert to a Map.<p>
     * Then it converts the Map to MultiValueMap.<p>
     */
    protected HttpServiceArgumentResolver objToMultiValueMapHttpServiceArgumentResolver() {
        return (argument, parameter, requestValues) -> {
            if (argument != null &&
                parameter.getParameterAnnotation(RequestBody.class) != null &&
                MediaType.APPLICATION_FORM_URLENCODED.equals(requestValues.build().getHeaders().getContentType()) &&
                !(argument instanceof MultiValueMap) &&
                ReactiveAdapterRegistry.getSharedInstance().getAdapter(parameter.getParameterType()) == null) {

                if (parameter.getParameterAnnotation(Valid.class) != null) {
                    HelperBeanValidation.validate(argument);
                }
                requestValues.setBodyValue(WebSpringHelper.converterObjToMultiValueMap(argument));

                return true;
            }

            return false;
        };
    }
}