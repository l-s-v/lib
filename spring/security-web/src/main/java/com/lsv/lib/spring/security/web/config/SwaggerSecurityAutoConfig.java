package com.lsv.lib.spring.security.web.config;

import com.lsv.lib.security.web.AllowHttpAccess;
import com.lsv.lib.security.web.helper.oidc.IssuerHelper;
import com.lsv.lib.security.web.properties.HttpMatcher;
import com.lsv.lib.security.web.properties.oidc.Issuer;
import com.lsv.lib.spring.security.web.annotation.ConditionalWebSecurityEnable;
import com.lsv.lib.spring.web.config.SwaggerAutoConfig;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import java.util.stream.Collectors;

import static com.lsv.lib.spring.core.helper.ConstantsSpring.SPRING_APPLICATION_NAME;

/**
 * Autoconfiguration of security for the swagger.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j

@ConditionalOnBean(OpenAPI.class)
@ConditionalWebSecurityEnable
@AutoConfiguration(after = {SwaggerAutoConfig.class, SpringSecurityWebAutoConfig.class})
public class SwaggerSecurityAutoConfig {

    /**
     * Frees access to swagger pages, adding one processing to run on the security part.
     */
    @Bean
    public AllowHttpAccess swaggerAllowHttpAccess(@Value("${springdoc.api-docs.path:/v3/api-docs}") String apiDocspath,
                                                  @Value("${springdoc.swagger-ui.path:/swagger-ui}") String swaggerUiPath) {

        return () -> {
            log.trace("Liberando acesso ao swagger");

            return HttpMatcher.withPatterns(
                swaggerUiPath + "*/**",
                apiDocspath + "/**",
                "/swagger-ui*/**");
        };
    }

    /**
     * Configures the authentications for the swagger api.
     */
    @SuppressWarnings(SPRING_APPLICATION_NAME)
    @Autowired
    public void configSecuritySwagger(OpenAPI openAPI, IssuerHelper issuerHelper) {
        log.trace("Registrando configurações de segurança para o swagger");

        if (issuerHelper.hasIssuers() &&
            issuerHelper.filterIssuerClientsApiDocs().isEmpty() &&
            issuerHelper.filterIssuerResourceServersApiDocs().isEmpty()) {

            throw new IllegalArgumentException("Deve haver ao menos uma issuer habilitada para autenticação nas api de documentação");
        }

        issuerHelper.filterIssuerClientsApiDocs().stream()
            .forEach(issuer -> addSecurity(openAPI, issuer, SecurityScheme.Type.OAUTH2));

        // For access_token just one is enough, because for swagger it doesn't make any difference where the user will get it
        issuerHelper.filterIssuerResourceServersApiDocs().stream()
            .findFirst()
            .ifPresentOrElse(issuer -> addSecurity(openAPI, issuer, SecurityScheme.Type.HTTP), () -> {
            });
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void addSecurity(OpenAPI openAPI, Issuer issuer, SecurityScheme.Type type) {
        if (openAPI.getComponents() == null) {
            openAPI.setComponents(new Components());
        }
        var name = "%s - %s".formatted(openAPI.getSecurity() != null
                ? openAPI.getSecurity().size() + 1
                : 1
            , type);

        openAPI
            .addSecurityItem(new SecurityRequirement().addList(name))
            .getComponents().addSecuritySchemes(name,
                createSecurityScheme(issuer, name, type)
            );
    }

    private SecurityScheme createSecurityScheme(Issuer issuer, String name, SecurityScheme.Type type) {
        var scheme = new SecurityScheme()
            .name(name)
            .type(type);

        if (SecurityScheme.Type.OAUTH2.equals(type)) {
            return scheme.flows(createFlowOauth2(issuer));
        }

        return scheme.scheme("bearer")
            .bearerFormat("JWT");
    }

    private OAuthFlows createFlowOauth2(Issuer issuer) {
        var scopes = new Scopes();
        scopes.putAll(issuer.getScopes().stream().collect(Collectors.toMap(scope -> scope, scope -> scope)));

        return new OAuthFlows()
            .authorizationCode(new OAuthFlow()
                .authorizationUrl(issuer.getEndPoints().getAuthorizationEndpoint())
                .refreshUrl(issuer.getEndPoints().getTokenEndpoint())
                .tokenUrl(issuer.getEndPoints().getTokenEndpoint())
                .scopes(scopes)
            );
    }
}