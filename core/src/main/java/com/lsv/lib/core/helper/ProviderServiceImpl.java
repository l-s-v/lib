package com.lsv.lib.core.helper;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.ProviderService;
import com.lsv.lib.core.pattern.register.RegisterInterface;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public final class ProviderServiceImpl {

    private static final int DATA_LIMIT = 100;
    private static final Map<Object, Stock<Repository<?>>> data = new HashMap<>();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings({"unchecked"})
    public static <I extends Identifiable<?>,  R extends Repository<I>> R repository(@NonNull ProviderService<I, R> provider) {
        Stock<Repository<?>> stock = stock(provider);
        if(stock.repository() == null){
            stock.repository(createRepository(provider));
        }
        return (R) stock.repository();
    }

    public static <I extends Identifiable<?>,  R extends Repository<I>> void repository(@NonNull ProviderService<I, R> provider, @NonNull R repository) {
        stock(provider).repository(repository);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static Stock<Repository<?>> stock(ProviderService<?, ?> provider) {
        Object key = key(provider);
        Stock<Repository<?>> stock = data.get(key);

        if(stock == null) {
            clearDataIfExceedsLimit();

            stock = new Stock<Repository<?>>();
            data.put(key, stock);
        }
        return stock;
    }

    private static Object key(ProviderService<?, ?> provider) {
        return System.identityHashCode(provider);
    }

    @SuppressWarnings({"unchecked"})
    private static <R> R createRepository(ProviderService<?, ?> provider) {
        return (R) RegisterInterface.findImplementation(HelperClass.identifyGenericsClass(provider, Repository.class));
    }

    private static void clearDataIfExceedsLimit() {
        if(data.size() > DATA_LIMIT) data.clear();
    }

    @Getter
    @Setter
    private static class Stock<R> {
        private R repository;
    }
}