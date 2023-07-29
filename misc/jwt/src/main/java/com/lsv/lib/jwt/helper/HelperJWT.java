package com.lsv.lib.jwt.helper;

import com.lsv.lib.core.concept.dto.Dto;
import com.lsv.lib.core.helper.HelperObj;
import com.lsv.lib.jwt.config.JwtProperties;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.ValidationException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Provides operations with JWT.
 * Used library: https://connect2id.com/products/nimbus-jose-jwt
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public final class HelperJWT {

    private JwtProperties jwtProperties;
    private Config config;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public HelperJWT(JwtProperties jwtProperties) {
        jwtProperties(jwtProperties);
        config(new Config(jwtProperties));
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// JWT (Json Web Token)

    public <T> T jwtToObject(String jwt, Class<T> classe) {
        try {
            return parseToObject(JOSEObject.parse(jwt), classe);
        } catch (ParseException e) {
            throw new ValidationException(String.format("Erro ao converter payload do JWT para objeto: %s", jwt), e);
        }
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// JWS (Json Web Signature)

    public String createJWS(JwtTokenizable jwtTokenizable, Map<String, Object> claims) {
        return sign(createClaimSet(jwtTokenizable, claims));
    }

    @SuppressWarnings("unchecked")
    public String createJWS(Dto dto) {
        return createJWS(HelperObj.convertValue(dto, Map.class));
    }

    public String createJWS(Map<String, Object> claims) {
        return sign(createClaimSet(claims));
    }

    public boolean verifySignature(String jws, String jwks) {
        return verifySignature(jws, findJWK(jws, jwks));
    }

    public boolean verifyAutoSignature(String jws) {
        return verifySignature(jws, getConfig().jwsVerifier);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// JWE - Json Web Encryption

    @SuppressWarnings("unchecked")
    public String createJWE(Dto dto) {
        return createJWE(HelperObj.convertValue(dto, Map.class));
    }

    public String createJWE(Map<String, Object> claims) {
        return encripty(createClaimSet(claims));
    }

    public <T> T jweToObject(String jwe, Class<T> classe) {
        try {
            var jweObject = JWEObject.parse(jwe);
            jweObject.decrypt(getConfig().jweDecrypter);
            return parseToObject(jweObject, classe);
        } catch (ParseException | JOSEException e) {
            throw new ValidationException("Erro ao decriptar o JWT", e);
        }
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// JWK - Json Web Key

    public String publicJWKS() {
        return getConfig().publicJwks;
    }

    public String loadJWKS(String url) {
        try {
            return JWKSet.load(new URL(url)).toString();
        } catch (Exception e) {
            throw new ValidationException(String.format("Erro ao recuperar JWKS de %s", url), e);
        }
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// Private

    private JWTClaimsSet createClaimSet(JwtTokenizable jwtTokenizable, Map<String, Object> claims) {
        var jWTClaimsSetBuilder = new JWTClaimsSet.Builder()
            .subject(jwtTokenizable.getSubject())
            .issuer(jwtTokenizable.getIssuer())
            .audience(jwtTokenizable.getAudience())
            .issueTime(new Date())
            .expirationTime(new Date(System.currentTimeMillis() + jwtProperties().getExpirationTimeSeconds() * 1000L))
            .jwtID(UUID.randomUUID().toString());

        claims.forEach(jWTClaimsSetBuilder::claim);

        return jWTClaimsSetBuilder.build();
    }

    private JWTClaimsSet createClaimSet(Map<String, Object> claims) {
        var jWTClaimsSetBuilder = new JWTClaimsSet.Builder();
        claims.forEach(jWTClaimsSetBuilder::claim);

        return jWTClaimsSetBuilder.build();
    }

    private boolean verifySignature(String jws, JWSVerifier verifier) {
        try {
            return JWSObject.parse(jws).verify(verifier);
        } catch (ParseException | JOSEException e) {
            throw new ValidationException("Erro ao verificar assinatura JWS", e);
        }
    }

    private JWSVerifier findJWK(String jws, String jwks) {
        try {
            var jwkSet = JWKSet.parse(jwks);

            List<JWK> jwkList = new JWKSelector(
                JWKMatcher.forJWSHeader(JWSObject.parse(jws).getHeader()))
                .select(jwkSet);

            if (jwkList == null || jwkList.isEmpty()) {
                throw new ValidationException("Não foi possível identificar a chave JWK utilizada.");
            }

            return new RSASSAVerifier(jwkList.get(0).toRSAKey());

        } catch (ParseException | JOSEException e) {
            throw new ValidationException("Erro ao verificar assinatura JWS", e);
        }
    }

    private <T> T parseToObject(JOSEObject jwt, Class<T> classe) {
        return HelperObj.convertValue(jwt.getPayload().toJSONObject(),classe);
    }

    private String sign(JWTClaimsSet jwtClaimsSet) {
        try {
            var jwt = new SignedJWT(getConfig().jwsHeader, jwtClaimsSet);
            jwt.sign(getConfig().jwsSigner);
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new ValidationException("Erro ao assinar o JWT", e);
        }
    }

    private String encripty(JWTClaimsSet jwtClaimsSet) {
        try {
            var jwe = new EncryptedJWT(getConfig().jweHeader, jwtClaimsSet);
            jwe.encrypt(getConfig().jweEncrypter);
            return jwe.serialize();
        } catch (JOSEException e) {
            throw new ValidationException("Erro ao encriptar o JWT", e);
        }
    }

    private Config getConfig() {
        if(config == null) {
            throw new IllegalCallerException("Configurações para trabalhar com JWT não foram iniciadas.");
        }
        return config;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Data
    private static class Config {

        private JWSHeader jwsHeader;
        private JWSSigner jwsSigner;
        private JWSVerifier jwsVerifier;

        private JWEHeader jweHeader;
        private JWEEncrypter jweEncrypter;
        private JWEDecrypter jweDecrypter;

        private String publicJwks;

        public Config(JwtProperties jwtProperties) {
            log.debug("Carregando configurações JWT");

            try {
                var jwkSet = JWKSet.parse(Base64.from(jwtProperties.getJwksBase64()).decodeToString());
                publicJwks(jwkSet.toPublicJWKSet().toString());

                var key = jwkSet.getKeys().get(0);
                var privateKey = key.toRSAKey();
                var publicKey = privateKey.toPublicJWK();

                jwsHeader(new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(key.getKeyID()).build());
                jwsSigner(new RSASSASigner(privateKey));
                jwsVerifier(new RSASSAVerifier(publicKey));

                jweHeader(new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM));
                jweEncrypter(new RSAEncrypter(publicKey));
                jweDecrypter(new RSADecrypter(privateKey));

            } catch (ParseException | JOSEException e) {
                throw new RuntimeException("Erro ao iniciar as configurações para trabalhar com JWT.", e);
            }
        }
    }
}