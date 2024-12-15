package com.lsv.lib.spring.jpa.repository;

import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.mapper.Mappable;
import com.lsv.lib.spring.core.converter.ConverterSpringJpa;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.lsv.lib.core.helper.HelperLog.trace;
import static com.lsv.lib.core.helper.HelperLog.warn;
import static com.lsv.lib.core.helper.HelperObj.toJsonString;

/**
 * Provides helper methods for Repository with Spring and JPA.
 *
 * @author Leandro da Silva Vieira
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HelperRepositorySpringJpa {

    public static <I extends Serializable, P, S extends JpaRepository<P, ?>>
    ListDto<I> findAllByFilter(S storable,
                               Mappable<I, P> mappable,
                               @NonNull Filter<I> filter) {

        return findAll(storable, mappable, null, filter);
    }

    public static <I extends Serializable, P, S extends JpaSpecificationExecutor<P>>
    ListDto<I> findAllByFilter(S storable,
                               Mappable<I, P> mappable,
                               Specification<P> specification,
                               @NonNull Filter<I> filter) {

        return findAll(storable, mappable, specification, filter);
    }

    public static Pageable extractPageable(@NonNull Filter<?> filter) {
        return ConverterSpringJpa.to(filter);
    }

    public static <I extends Serializable, P> ListDto<I> toListDtoPageable(Mappable<I, P> mappable, Supplier<Page<P>> pageProducer) {
        return toListDtoPageable(mappable, pageProducer.get());
    }

    public static <I extends Serializable, P> ListDto<I> toListDtoPageable(Mappable<I, P> mappable, Page<P> page) {
        return Optional
            .ofNullable(page)
            .map(ps -> ConverterSpringJpa.of(page, mappable))
            .orElse(ListDto.empty());
    }

    public static <I extends Serializable, P> ListDto<I> toListDto(Mappable<I, P> mappable, Supplier<List<P>> listProducer) {
        return toListDto(mappable, listProducer.get());
    }

    public static <I extends Serializable, P> ListDto<I> toListDto(Mappable<I, P> mappable, List<P> results) {
        return Optional
            .ofNullable(results)
            .map(ps -> ListDto.of(mappable.of(ps)).get())
            .orElse(ListDto.empty());
    }

    public static <I extends Serializable> ListDto<I> toListDto(List<I> results) {
        return Optional
            .ofNullable(results)
            .map(ps -> ListDto.of(results).get())
            .orElse(ListDto.empty());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private static <I extends Serializable, P, S> ListDto<I> findAll(S storable,
                                                                     Mappable<I, P> mappable,
                                                                     Specification<P> specification,
                                                                     @NonNull Filter<I> filter) {

        trace(log, "findAllByFilter = {}", () -> toJsonString(filter));

        var pageable = extractPageable(filter);
        Example<P> example = null;

        if (specification == null) {
            try {
                var obj = filter.getObj(HelperClass.identifyGenericsClass(mappable.getClass().getGenericInterfaces()[0], 0));
                if (obj != null) {
                    example = Example.of(mappable.to(obj));
                }
            } catch (Exception e) {
                warn(log, "Unable to prepare the Example based on the specified object", () -> e);
            }
        }

        if (pageable != null) {
            return Optional
                .of(findAllPageable(storable, example, specification, pageable))
                .map(pageSupplier -> toListDtoPageable(mappable, pageSupplier))
                .orElseThrow();
        } else {
            return Optional
                .of(findAllSorted(storable, example, specification, ConverterSpringJpa.to(filter.getSorts())))
                .map(listSupplier -> toListDto(mappable, listSupplier))
                .orElseThrow();
        }
    }

    @SuppressWarnings("unchecked")
    private static <P, S>
    Supplier<Page<P>> findAllPageable(S storable,
                                      Example<P> example,
                                      Specification<P> specification,
                                      @NonNull Pageable pageable) {
        return () -> {
            if (specification != null) {
                return ((JpaSpecificationExecutor<P>) storable).findAll(specification, pageable);
            } else if (example != null) {
                return ((JpaRepository<P, ?>) storable).findAll(example, pageable);
            } else {
                return ((JpaRepository<P, ?>) storable).findAll(pageable);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <P, S>
    Supplier<List<P>> findAllSorted(S storable,
                                    Example<P> example,
                                    Specification<P> specification,
                                    @NonNull Sort sort) {

        return () -> {
            if (specification != null) {
                return ((JpaSpecificationExecutor<P>) storable).findAll(specification, sort);
            } else if (example != null) {
                return ((JpaRepository<P, ?>) storable).findAll(example, sort);
            } else {
                return ((JpaRepository<P, ?>) storable).findAll(sort);
            }
        };
    }
}
