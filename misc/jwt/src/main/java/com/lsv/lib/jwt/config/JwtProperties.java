package com.lsv.lib.jwt.config;

import com.lsv.lib.core.helper.LibConstants;
import com.lsv.lib.core.properties.LibProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Makes externalizing configurations easier with Spring.
 *
 * @author Leandro da Silva Vieira
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtProperties implements LibProperties {

    public static final String PATH = LibConstants.BASE_LIB_PROPERTIES + "jwt";

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Chave JWKS utilizada pela aplicação. Deve conter apenas uma chave no array.
     * O parâmetro deve estar codificado em base 64. Para geração durante o desenvolvimento, foi utilizado o site
     * https://mkjwk.org/ com os seguintes parâmetros: Key Size=2048, Key Use=Signature, Algorithm=RS256, Key ID=SHA-256;
     */
    @NotBlank
    private String jwksBase64;
    /**
     * Tempo de expiração, em segundos, do jwt gerado. Por padrão é 30s
     */
    private Long expirationTimeSeconds = 30l;
}