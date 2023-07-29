package com.lsv.lib.spring.core.annotation;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.lang.annotation.*;

import static com.lsv.lib.core.helper.LibConstants.DEFAULT_ENCODING;

/**
 * Allows using @PropertySource with yaml files.
 *
 * https://www.baeldung.com/spring-yaml-propertysource
 *
 * @author Leandro da Silva Vieira
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@org.springframework.context.annotation.PropertySource(
    value = {},
    factory = YamlSource.YamlSourceFactory.class
)
public @interface YamlSource {

    @AliasFor(annotation = org.springframework.context.annotation.PropertySource.class, attribute = "value")
    String value();

    @AliasFor(annotation = org.springframework.context.annotation.PropertySource.class, attribute = "ignoreResourceNotFound")
    boolean ignoreResourceNotFound() default true;

    @AliasFor(annotation = org.springframework.context.annotation.PropertySource.class, attribute = "encoding")
    String encoding() default DEFAULT_ENCODING;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    class YamlSourceFactory implements PropertySourceFactory {

        @Override
        public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(encodedResource.getResource());

            return new PropertiesPropertySource(encodedResource.getResource().getFilename(), factory.getObject());
        }
    }
}