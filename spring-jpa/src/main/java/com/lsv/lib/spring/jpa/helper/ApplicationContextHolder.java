package com.lsv.lib.spring.jpa.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Permite recuperar os objetos do contexto do Spring sem utilizar @Autowired.
 *
 * @author Leandro da Silva Vieira
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

// ***********************************************************************************************************************************

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        synchronized (this) {
            if (ApplicationContextHolder.applicationContext == null) {
                ApplicationContextHolder.applicationContext = applicationContext;
            }
        }
    }

// ***********************************************************************************************************************************

    public static <T> T getBean(Class<T> classe) {
        return applicationContext.getBean(classe);
    }
}