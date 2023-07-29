package com.lsv.lib.jwt.helper;

/**
 * Define o padrão dos objetos que poderão ser transformados em um token.
 *
 * @author Leandro da Silva Vieira
 */
public interface JwtTokenizable {

    String getSubject();
    String getIssuer();
    String getAudience();
}