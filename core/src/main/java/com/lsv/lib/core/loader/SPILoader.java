package com.lsv.lib.core.loader;

import com.google.auto.service.AutoService;
import com.lsv.lib.core.annotation.Priority;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.ServiceLoader.Provider;

/**
 * SPI implementation for Loadable.
 *
 * @author Leandro da Silva Vieira
 */
@Priority
@AutoService(Loadable.class)
public final class SPILoader implements Loadable {

    @Override
    public <T> Collection<T> load(Class<T> classType) {
        // Informa que o módulo atual utiliza a interface
        getClass().getModule().addUses(classType);
        return ServiceLoader.load(classType).stream().map(Provider::get).toList();
    }
}