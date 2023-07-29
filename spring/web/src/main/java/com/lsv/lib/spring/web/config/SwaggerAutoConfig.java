package com.lsv.lib.spring.web.config;

import com.lsv.lib.spring.web.properties.SwaggerProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Autoconfiguration for the swagger.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j

@ConditionalOnProperty(
    prefix = "springdoc.api-docs",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true
)
@AutoConfiguration(after = {SpringWebMvcAutoConfig.class})
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerAutoConfig {

    /**
     * Configures the information displayed on the swagger home screen.
     */
    @Bean
    @ConditionalOnMissingBean
    public OpenAPI defaultConfigSwagger(SwaggerProperties swaggerProperties) {
        log.trace("Registrando configurações do swagger");

        return new OpenAPI(SpecVersion.V31)
            .info(new Info()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .termsOfService(swaggerProperties.getUriTermsOfService())
                .version(swaggerProperties.getVersion())
                .contact(new Contact()
                    .name(swaggerProperties.getContact().getName())
                    .url(swaggerProperties.getContact().getUrl())
                    .email(swaggerProperties.getContact().getEmail())
                )
        );
    }
}