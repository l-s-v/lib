package com.lsv.lib.spring.security.web.extraprocess;

import com.lsv.lib.core.function.ExtraProcess;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Defines the extra process for configuring resource server.
 *
 * @author Leandro da Silva Vieira
 */
public interface ResourceServerExtraProcess extends ExtraProcess<HttpSecurity> {
}