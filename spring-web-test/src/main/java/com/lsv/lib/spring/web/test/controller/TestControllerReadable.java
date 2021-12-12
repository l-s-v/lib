package com.lsv.lib.spring.web.test.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import com.lsv.lib.spring.core.ConverterSpringJpa;
import com.lsv.lib.spring.web.controller.ReadableController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface TestControllerReadable<
    IN extends Identifiable<?>,
    OUT extends Identifiable<?>,
    S extends Service<OUT> & Readable<OUT>>
    extends
    TestController<IN, OUT, S> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
            Stream.of(
                findById(),
                findByIdNoFound(),
                findByFilterWithoutParams(),
                findByFilterWithParamsPageable()),
            TestController.super.of());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicTest findById() {
        return DynamicTest.dynamicTest("findById", () -> {
            when(serviceMock().findById(any()))
                .thenReturn(Optional.of(expectedObjectOut()));

            IN objIn = newObjectIn();
            performInContext(get(urlBaseWithId(), objIn.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper().writeValueAsString(objIn)));
        });
    }

    private DynamicTest findByIdNoFound() {
        return DynamicTest.dynamicTest("findByIdNoFound", () -> {
            when(serviceMock().findById(any()))
                .thenReturn(Optional.empty());

            performInContext(get(urlBaseWithId(), newObjectIn().getId().toString()))
                .andExpect(status().isNotFound());
        });
    }

    private DynamicTest findByFilterWithoutParams() {
        return DynamicTest.dynamicTest("findByFilterWithoutParams", () -> {
            ListDto<OUT> listDto = ListDto.of(List.of(expectedObjectOut())).get();

            when(serviceMock().findByFilter(any()))
                .thenReturn(listDto);

            Page<OUT> page = ConverterSpringJpa.to(listDto, ReadableController.PAGEABLE_DEFAULT);

            performInContext(get(urlBase()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper().writeValueAsString(page)));
        });
    }

    private DynamicTest findByFilterWithParamsPageable() {
        return DynamicTest.dynamicTest("findByFilterWithParamsPageable", () -> {
            ListDto<OUT> listDto = ListDto.of(List.of(expectedObjectOut())).get();
            Filter<?> filter = createFilter();

            when(serviceMock().findByFilter(any()))
                .thenAnswer(i -> {
                    // if the filter was converted correctly
                    Assertions.assertEquals(filter.toString(), i.getArguments()[0].toString());
                    return listDto;
                });

            Page<OUT> page = ConverterSpringJpa.to(listDto, ConverterSpringJpa.to(filter));

            performInContext(get(urlBase())
                .param("page", String.valueOf(filter.page().numPage()))
                .param("size", String.valueOf(filter.page().size()))
                .param("sort", filter.orderBies().get(0).property() + "," +
                    String.valueOf(filter.orderBies().get(0).asc() ? Sort.Direction.ASC : Sort.Direction.DESC).toLowerCase()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper().writeValueAsString(page)));
        });
    }

    private Filter<?> createFilter() {
        return Filter.of(null)
            .page(Filter.Page.of()
                .numPage(2)
                .size(20)
                .get())
            .orderBy(Filter.OrderBy.of()
                .property("nome")
                .asc(false)
                .get())
            .get();
    }
}