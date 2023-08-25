package com.lsv.lib.spring.web.resilient4j.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lsv.lib.core.function.Resolver;
import com.lsv.lib.core.helper.HelperObj;
import com.lsv.lib.spring.web.commons.event.WebClientRequestInterceptEvent;
import com.lsv.lib.spring.web.resilient4j.properties.R4jInstance;
import com.lsv.lib.spring.web.resilient4j.properties.R4jModuleProperties;
import com.lsv.lib.spring.web.resilient4j.properties.R4jProperties;
import com.lsv.lib.spring.web.resilient4j.properties.R4jProperties.Match;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpMethod;

import java.util.*;
import java.util.function.Predicate;

import static com.lsv.lib.core.helper.HelperLog.trace;
import static com.lsv.lib.core.helper.HelperLog.warn;

/**
 * Binds the WebClient request data to the Resiliente4J settings.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
public class R4jOperatorResolverByEvent implements Resolver<WebClientRequestInterceptEvent, Collection<R4jOperator<?>>> {

    private final R4jModuleProperties r4jModuleProperties;
    private final Resolver<R4jInstance, R4jOperator<?>> operatorResolverByInstance;
    /**
     * Configurations are static, being built only when the project starts.<p>
     * The cache will help to not have to process the same data all the time and will keep only the most used data in memory.
     */
    private final Cache<KeyEvent, Collection<R4jOperator<?>>> cacheConfigByEvent;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public R4jOperatorResolverByEvent(R4jModuleProperties r4jModuleProperties, Resolver<R4jInstance, R4jOperator<?>> operatorResolverByInstance) {
        this.r4jModuleProperties = r4jModuleProperties;
        this.operatorResolverByInstance = operatorResolverByInstance;

        cacheConfigByEvent = Caffeine.newBuilder()
            .expireAfterAccess(r4jModuleProperties.getCacheOperator().getDuration())
            .maximumSize(r4jModuleProperties.getCacheOperator().getSize())
            .build();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    @Override
    public Collection<R4jOperator<?>> resolve(WebClientRequestInterceptEvent event) {
        return
            cacheConfigByEvent.get(
                KeyEvent.of(event),
                keyEvent -> (Collection<R4jOperator<?>>) (Object)
                    resolveByR4jModuleProperties(keyEvent).stream()
                        .map(operatorResolverByInstance::resolve)
                        .toList()
            );
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private Collection<R4jInstance> resolveByR4jModuleProperties(KeyEvent event) {
        var r4jPropertiesList = new LinkedList<>(r4jModuleProperties.getConfigurations());
        if (r4jModuleProperties.getDefaultConfig() != null) {
            r4jPropertiesList.add(r4jModuleProperties.getDefaultConfig());
        }

        var r4jInstances = resolveByR4jPropertiesList(r4jPropertiesList, event);
        trace(log, () -> "\"Configurações aplicáveis: \n\n%s\n".formatted(HelperObj.toString(r4jInstances)));

        return r4jInstances;
    }

    private Collection<R4jInstance> resolveByR4jPropertiesList(List<R4jProperties> r4jPropertiesList, KeyEvent event) {
        return r4jPropertiesList.stream()
            .map(r4jProperties -> resolveByR4jProperties(r4jProperties, event))
            .flatMap(List::stream)
            .filter(filterDuplicatesAndLog())
            .toList();
    }

    private Predicate<R4jInstance> filterDuplicatesAndLog() {
        var unrepeated = new HashSet<R4jInstance>();

        return r4jInstance -> {
            if (!unrepeated.add(r4jInstance)) {
                warn(log, () -> "Já existe uma configuração de %s para os mesmos requisitos. Será ignorada e pertence à configuração:\n\n%s\n"
                    .formatted(r4jInstance.getType(), HelperObj.toString(r4jInstance.r4jProperties())));
                return false;
            }
            return true;
        };
    }

    private List<R4jInstance> resolveByR4jProperties(R4jProperties r4jProperties, KeyEvent event) {
        return match(r4jProperties.getMatch(), event)
            ? new ArrayList<>(r4jProperties.getInstances())
            : Collections.emptyList();
    }

    private boolean match(Match match, KeyEvent event) {
        // If nothing is reported
        if (ObjectUtils.allNull(match.getPackageName(), match.getClientClass(), match.getHttpMethod(), match.getUri())) {
            return true;
        }
        if (match.getPackageName() != null && !event.clientClass().getPackageName().startsWith(match.getPackageName())) {
            return false;
        }
        if (match.getClientClass() != null && !event.clientClass().equals(match.getClientClass())) {
            return false;
        }
        if (match.getHttpMethod() != null && ObjectUtils.compare(event.httpMethod(), match.getHttpMethod()) != 0) {
            return false;
        }
        if (match.getUri() != null && ObjectUtils.compare(event.uri(), match.getUri()) != 0) {
            return false;
        }

        return true;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    @Getter
    public static class KeyEvent {
        private Class<?> clientClass;
        private HttpMethod httpMethod;
        private String uri;

        public static KeyEvent of(WebClientRequestInterceptEvent event) {
            return new KeyEvent(
                event.clientClass(),
                event.requestValues().getHttpMethod(),
                event.requestValues().getUriTemplate()
            );
        }
    }
}