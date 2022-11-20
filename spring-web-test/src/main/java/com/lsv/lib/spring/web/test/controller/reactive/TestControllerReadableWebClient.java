package com.lsv.lib.spring.web.test.controller.reactive;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.spring.core.ConverterSpringJpa;
import com.lsv.lib.spring.web.controller.ReadableController;
import com.lsv.lib.spring.web.test.controller.TestControllerReadable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

import static com.lsv.lib.spring.web.test.controller.TestControllerReadable.createFilter;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public interface TestControllerReadableWebClient<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT> & Readable<OUT>>
        extends
        TestControllerReadable<IN, OUT, S>,
        TestControllerWebClient<IN, OUT, S> {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    default Executable execFindByIde() {
        return () -> {
            when(service().findById(any()))
                    .thenReturn(Optional.of(expectedObjectOut()));

            webTestClient()
                    .get()
                    .uri(urlBaseWithId(), newObjectIn().getId().toString())
                    .exchange()
                    .expectStatus().isOk();
        };
    }

    @Override
    default Executable execFindByIdNoFound() {
        return () -> {
            when(service().findById(any()))
                    .thenReturn(Optional.empty());

            webTestClient()
                    .get()
                    .uri(urlBaseWithId(), newObjectIn().getId().toString())
                    .exchange()
                    .expectStatus().isNotFound();
        };
    }

    @Override
    default Executable execFindByFilterWithoutParams() {
        return () -> {
            ListDto<OUT> listDto = ListDto.of(List.of(expectedObjectOut())).get();

            when(service().findByFilter(any()))
                    .thenReturn(listDto);

            Page<OUT> page = ConverterSpringJpa.to(listDto, ReadableController.PAGEABLE_DEFAULT);

            webTestClient()
                    .get()
                    .uri(urlBase())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody().json(objectMapper().writeValueAsString(page))
            ;
        };
    }

    @Override
    default Executable execFindByFilterWithParamsPageable() {
        return () -> {
            ListDto<OUT> listDto = ListDto.of(List.of(expectedObjectOut())).get();
            Filter<?> filter = createFilter();

            when(service().findByFilter(any()))
                    .thenAnswer(i -> {
                        // if the filter was converted correctly
                        Assertions.assertEquals(filter.toString(), i.getArguments()[0].toString());
                        return listDto;
                    });

            Page<OUT> page = ConverterSpringJpa.to(listDto, ConverterSpringJpa.to(filter));

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("page", String.valueOf(filter.page().numPage()));
            params.add("size", String.valueOf(filter.page().size()));
            params.add("sort", filter.orderBies().get(0).property() + "," +
                    String.valueOf(filter.orderBies().get(0).asc() ? Sort.Direction.ASC : Sort.Direction.DESC).toLowerCase());

            webTestClient()
                    .get()
                    .uri(UriComponentsBuilder
                            .fromUriString(urlBase())
                            .queryParams(params)
                            .build().toUri())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody().json(objectMapper().writeValueAsString(page));
        };
    }
}