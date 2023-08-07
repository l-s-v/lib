package com.lsv.lib.spring.jpa.config;

import com.lsv.lib.spring.core.annotation.YamlSource;
import com.lsv.lib.spring.core.config.SpringCoreAutoConfig;
import com.lsv.lib.spring.jpa.properties.JpaProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Autoconfiguration for the jpa module.
 *
 * @author Leandro da Silva Vieira
 */
@YamlSource("classpath:/jpa.yaml")
@AutoConfiguration(after = {SpringCoreAutoConfig.class})
@EnableConfigurationProperties(JpaProperties.class)
public class JpaAutoConfig {
}