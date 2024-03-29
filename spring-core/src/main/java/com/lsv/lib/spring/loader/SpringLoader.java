package com.lsv.lib.spring.loader;

import com.google.auto.service.AutoService;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.loader.Loadable;
import lombok.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AutoService(Loadable.class)
@Component
public class SpringLoader implements ApplicationContextAware, Loadable {

    private static ApplicationContext applicationContext;
    private static ConfigurableListableBeanFactory beanFactory;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        synchronized (this) {
            if (SpringLoader.applicationContext == null) {
                SpringLoader.applicationContext = applicationContext;
                beanFactory = ((ConfigurableApplicationContext) SpringLoader.applicationContext).getBeanFactory();
            }
        }
    }


// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public <T> Collection<T> load(Class<T> classType) {
        Map<String, T> dados = applicationContext().getBeansOfType(classType);
        return dados == null ? null : dados.values();
    }

    @Override
    public Integer priority() {
        return 1;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <T> T bean(Class<T> classe) {
        return applicationContext().getBean(classe);
    }

    public static <T> T bean(Object sourceBase, Class<T> classe) {
        return bean(HelperClass.identifyGenericsClass(sourceBase, classe));
    }

    public static <T> void registerBean(Class<T> classe, @NonNull T bean) {
        beanFactory.registerResolvableDependency(classe, bean);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static ApplicationContext applicationContext() {
        if (applicationContext == null) {
            throw new NullPointerException("Contexto do spring não carregado.");
        }
        return applicationContext;
    }
}