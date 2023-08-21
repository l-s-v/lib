package com.lsv.lib.spring.web.commons.helper;

import com.lsv.lib.core.helper.HelperObj;
import com.lsv.lib.spring.core.loader.SpringLoader;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Provides useful methods for working with spring web.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WebSpringHelper {

    private static final Environment environment = SpringLoader.bean(Environment.class);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    public static MultiValueMap<String, Object> converterObjToMultiValueMap(Object obj) {
        return converterMapToMultiValueMap(HelperObj.convertValue(obj, Map.class));
    }

    public static MultiValueMap<String, Object> converterMapToMultiValueMap(Map<String, Object> valores) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.setAll(valores);
        return formData;
    }

    /**
     * I couldn't find another way to check this information at runtime.
     * The idea came from by OnWebApplicationCondition#isAnyWebApplication(ConditionContext, boolean).
     */
    public static boolean isWebApplicationServlet() {
        return environment instanceof ConfigurableWebEnvironment;
    }

    public static HttpServletRequest getCurrentHttpRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    public static Map<String, String> getAllHeaders(HttpServletRequest httpServletRequest) {
        return httpServletRequest == null
            ? Collections.emptyMap()
            : StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(httpServletRequest.getHeaderNames().asIterator(), Spliterator.ORDERED)
                , false
            )
            .map(s -> Map.of(s, httpServletRequest.getHeader(s)))
            .flatMap(map -> map.entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}