package com.lsv.lib.spring.security.web.resourceserver.resolver;

import com.lsv.lib.core.function.Resolver;
import com.lsv.lib.security.web.properties.oidc.Issuer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.time.Instant;

/**
 * Resolver an JwtDecoder based on the issuer.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
public class CacheJwksResolverByIssuer implements Resolver<Issuer, Cache> {

    @Override
    public Cache resolve(Issuer issuer) {
        return new JwtDecoderCache(issuer);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Simple caching just to allow for some configuration, as by default spring cache the jwks only for 5 minutes.
     *
     * Idea based from the resource available in the keycloak adapter (JWKPublicKeyLocator), also taking advantage
     * of its two available parameters (min-time-between-jwks-requests and public-key-cache-ttl).
     * https://www.keycloak.org/docs/18.0/securing_apps/#_java_adapter_config
     */
    private static class JwtDecoderCache extends ConcurrentMapCache {
        private Issuer issuer;

        private volatile long lastRequestTimeSeconds = 0;

        public JwtDecoderCache(Issuer issuer) {
            super("jwks_%s".formatted(issuer.getIssuerUri()));
            this.issuer = issuer;

            log.trace("Aplicando controle de cache para chave jwks");
        }

        @Override
        protected Object lookup(Object key) {
            var currentTimeSeconds = Instant.now().getEpochSecond();

            var newRequestIsBloqued = currentTimeSeconds < lastRequestTimeSeconds + issuer.getExtraConfig().getJwksMinTimeBetweenRequests();
            var cacheIsValid = currentTimeSeconds < lastRequestTimeSeconds + issuer.getExtraConfig().getJwksMaxTimeCached();

            log.trace("{} - nova requisição bloqueada = {}", key, newRequestIsBloqued);
            log.trace("{} - cache ainda é válido = {}", key, cacheIsValid);

            if ((newRequestIsBloqued || cacheIsValid) && key != null) {
                var obj = super.lookup(key);
                log.trace("{} - jwks recuperada do cache = {}", key, obj);
                return obj;
            } else {
                log.trace("{} - forçando atualização da jwks", key);
                return null;
            }
        }

        @Override
        public void put(Object key, Object value) {
            log.trace("{} - nova jwks recebida = {}", key, value);

            lastRequestTimeSeconds = Instant.now().getEpochSecond();
            super.put(key, value);
        }
    }
}