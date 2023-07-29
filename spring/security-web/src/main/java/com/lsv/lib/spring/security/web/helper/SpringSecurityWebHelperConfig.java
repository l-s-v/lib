package com.lsv.lib.spring.security.web.helper;

import com.lsv.lib.core.loader.Loader;
import com.lsv.lib.security.web.helper.oidc.IssuerHelper;
import com.lsv.lib.spring.security.web.extraprocess.ClientLoginExtraProcess;
import com.lsv.lib.spring.security.web.extraprocess.ResourceServerExtraProcess;
import lombok.RequiredArgsConstructor;

/**
 * Helps with setup operations without the main class having to worry about which dependencies are needed.
 *
 * @author Leandro da Silva Vieira
 */
@RequiredArgsConstructor
public class SpringSecurityWebHelperConfig {

    private final IssuerHelper issuerHelper;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public void validateConfig() {
        if (issuerHelper.hasIssuerClients()) {
            try {
                Loader.of(ClientLoginExtraProcess.class)
                    .findUniqueImplementationByFirstLoader();
            } catch (Exception e) {
                throw new IllegalArgumentException("Configuração inválida utilizando enabledClient=true.", e);
            }
        }
        if (issuerHelper.hasIssuerResourceServers()) {
            try {
                Loader.of(ResourceServerExtraProcess.class)
                    .findUniqueImplementationByFirstLoader();
            } catch (Exception e) {
                throw new IllegalArgumentException("Configuração inválida utilizando enabledResourceServer=true.", e);
            }
        }
        if (issuerHelper.hasIssuers() &&
            !issuerHelper.hasIssuerClients() &&
            !issuerHelper.hasIssuerResourceServers()) {

            throw new IllegalArgumentException("Deve haver ao menos uma issuer configurada com enabledClient=true ou enabledResourceServer=true.");
        }
    }
}