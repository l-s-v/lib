package com.lsv.lib.spring.web.test.controller.servlet;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.spring.core.converter.ConverterSpringJpa;
import com.lsv.lib.spring.web.controller.CrudController;
import com.lsv.lib.spring.web.test.controller.TestControllerReadable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static com.lsv.lib.spring.web.test.controller.TestControllerReadable.createFilter;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface TestControllerReadableMockMvc<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Readable<OUT>>
        extends
        TestControllerReadable<IN, OUT, S>,
        TestControllerMockMvc<IN, OUT, S> {

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


    @Override
    default Executable execFindByIde() {
        return () -> {
            when(service().findById(any()))
                    .thenReturn(Optional.of(expectedObjectOut()));

            IN objIn = newObjectIn();
            performInContext(get(urlBaseWithId(), objIn.getId().toString()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper().writeValueAsString(objIn)));
        };
    }

    @Override
    default Executable execFindByIdNoFound() {
        return () -> {
            when(service().findById(any()))
                    .thenReturn(Optional.empty());

            performInContext(get(urlBaseWithId(), newObjectIn().getId().toString()))
                    .andExpect(status().isNotFound());
        };
    }

    @Override
    default Executable execFindByFilterWithoutParams() {
        return () -> {
            ListDto<OUT> listDto = ListDto.of(List.of(expectedObjectOut())).get();

            when(service().findByFilter(any()))
                    .thenReturn(listDto);

            Page<OUT> page = ConverterSpringJpa.to(listDto, CrudController.PAGEABLE_DEFAULT);

            performInContext(get(urlBase()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper().writeValueAsString(page)));
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

            performInContext(get(urlBase())
                    .param("page", String.valueOf(filter.page().numPage()))
                    .param("size", String.valueOf(filter.page().size()))
                    .param("sort", filter.orderBies().get(0).property() + "," +
                            String.valueOf(filter.orderBies().get(0).asc() ? Sort.Direction.ASC : Sort.Direction.DESC).toLowerCase()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper().writeValueAsString(page)));
        };
    }
}