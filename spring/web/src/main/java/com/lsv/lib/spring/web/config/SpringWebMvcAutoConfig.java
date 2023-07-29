package com.lsv.lib.spring.web.config;

import com.lsv.lib.security.web.AllowHttpAccess;
import com.lsv.lib.security.web.properties.HttpMatcher;
import com.lsv.lib.spring.core.config.SpringCoreAutoConfig;
import com.lsv.lib.spring.web.controller.MvcErrorController;
import com.lsv.lib.spring.web.properties.SpringWebProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * @author Leandro da Silva Vieira
 */
@Slf4j
@RequiredArgsConstructor

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfiguration(after = SpringCoreAutoConfig.class)
@EnableConfigurationProperties(SpringWebProperties.class)
@EnableWebMvc
public class SpringWebMvcAutoConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeInterceptor());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Bean
    @ConditionalOnMissingBean
    public LocaleResolver defaultLocaleResolver() {
        return new CookieLocaleResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public LocaleChangeInterceptor localeInterceptor() {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("lang");
        return localeInterceptor;
    }

    /**
     * Releases access to the error page, adding processing to run on the security side.
     * It also frees access to the favicon.
     */
    @Bean
    public AllowHttpAccess webMvcAllowHttpAccess(@Value(MvcErrorController.REQUEST_MAPPING) String errorControllerPah) {

        return () -> {
            log.trace("Liberando acesso a página de erro '{}'", errorControllerPah);

            return HttpMatcher.withPatternsAndMethod(HttpMethod.GET.name(), errorControllerPah, "favicon*");
        };
    }
}