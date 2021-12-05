package com.lsv.lib.core.helper;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.ProviderService;
import com.lsv.lib.core.pattern.register.RegisterByInterface;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;
import java.util.WeakHashMap;

public final class ProviderServiceImpl {

    private static final int DATA_LIMIT = 100;
    private static final Map<Object, Stock<Repository<?>>> data = new WeakHashMap<>();

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
        Stock<Repository<?>> stock = data.get(provider);

        if(stock == null) {
            stock = new Stock<Repository<?>>();
            data.put(provider, stock);
        }
        return stock;
    }

    @SuppressWarnings({"unchecked"})
    private static <R> R createRepository(ProviderService<?, ?> provider) {
        return (R) RegisterByInterface.findImplementation(HelperClass.identifyGenericsClass(provider, Repository.class));
    }

    @Getter
    @Setter
    private static class Stock<R> {
        private R repository;
    }
}