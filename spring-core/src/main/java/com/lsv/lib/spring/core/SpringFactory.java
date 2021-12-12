package com.lsv.lib.spring.core;

import com.lsv.lib.core.helper.HelperClass;
import lombok.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Component
public class SpringFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static ConfigurableListableBeanFactory beanFactory;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        synchronized (this) {
            if (SpringFactory.applicationContext == null) {
                SpringFactory.applicationContext = applicationContext;
                beanFactory = ((ConfigurableApplicationContext) SpringFactory.applicationContext).getBeanFactory();
            }
        }
    }

    public static <T> T bean(Class<T> classe) {
        return applicationContext.getBean(classe);
    }

    public static <T> T bean(Object sourceBase, Class<T> classe) {
        return bean(HelperClass.identifyGenericsClass(sourceBase, classe));
    }

    public static <T> void registerBean(Class<T> classe, @NonNull T bean) {
        beanFactory.registerResolvableDependency(classe, bean);
    }
}