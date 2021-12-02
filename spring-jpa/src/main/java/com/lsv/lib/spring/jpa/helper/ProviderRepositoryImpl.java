package com.lsv.lib.spring.jpa.helper;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mapper;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.pattern.register.RegisterInterface;
import com.lsv.lib.spring.jpa.repository.ProviderRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.Map;

public final class ProviderRepositoryImpl {

    private static final int DATA_LIMIT = 100;
    private static final Map<Object, Stock<Mapper<?, ?>, JpaRepository<?, ?>>> data = new HashMap<>();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings({"unchecked"})
    public static <T, P> Mapper<T, P> mapper(@NonNull ProviderRepository<?, ?, ?> provider) {
        Stock<?, ?> stock = stock(provider);
        if(stock.mapper() == null){
            stock.mapper(createMapper(provider));
        }
        return (Mapper<T, P>) stock.mapper();
    }

    public static <T, P> void mapper(@NonNull ProviderRepository<?, ?, ?> provider, Mapper<T, P> mapper) {
        stock(provider).mapper(mapper);
    }

    @SuppressWarnings({"unchecked"})
    public static <P, ID> JpaRepository<P, ID> jpaRepository(@NonNull ProviderRepository<?, ?, ?> provider) {
        Stock<?, ?> stock = stock(provider);
        if(stock.jpaRepository() == null){
            stock.jpaRepository(createJpaRepository());
        }
        return (JpaRepository<P, ID>) stock.jpaRepository();
    }

    public static <P, ID> void jpaRepository(@NonNull ProviderRepository<?, ?, ?> provider, JpaRepository<P, ID> jpaRepository) {
        stock(provider).jpaRepository(jpaRepository);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static Stock<Mapper<?, ?>, JpaRepository<?, ?>> stock(ProviderRepository<?, ?, ?> provider) {
        Object key = key(provider);
        Stock<Mapper<?, ?>, JpaRepository<?, ?>> stock = data.get(key);

        if(stock == null) {
            clearDataIfExceedsLimit();

            stock = new Stock<>();
            data.put(key, stock);
        }
        return stock;
    }

    private static Object key(ProviderRepository<?, ?, ?> provider) {
        return System.identityHashCode(provider);
    }

    @SuppressWarnings({"unchecked"})
    private static <M> M createMapper(ProviderRepository<?, ?, ?> provider) {
        return (M) RegisterInterface.findImplementation(Mapper.class)
            .setup(
                HelperClass.identifyGenericsClass(provider, Identifiable.class),
                HelperClass.identifyGenericsClass(provider, Persistable.class)
            );
    }

    @SuppressWarnings({"unchecked"})
    private static <J> J createJpaRepository() {
        return (J) ApplicationContextHolder.getBean(JpaRepository.class);
    }

    private static void clearDataIfExceedsLimit() {
        if(data.size() > DATA_LIMIT) data.clear();
    }

    @Getter
    @Setter
    private static class Stock<M, J> {
        private M mapper;
        private J jpaRepository;
    }
}