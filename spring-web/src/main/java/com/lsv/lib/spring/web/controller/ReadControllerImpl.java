package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.helper.Log;
import com.lsv.lib.spring.core.ConverterSpringJpa;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class ReadControllerImpl<
        IN extends Identifiable<ID>,
        OUT extends Identifiable<ID>,
        S extends Service<OUT> & Readable<OUT>,
        ID extends Serializable>
        implements
        Controller<IN, OUT, S> {

    private final S service;
    private final Mappable<IN, OUT> mappable;
    private final Class<OUT> outClass;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public ResponseEntity<IN> findById(ID id) {
        log.debug("findById {}", id);

        OUT objOut = service().findById(Identifiable.of(outClass, id)).orElseThrow();
        IN objIN;

        if (mappable == null) {
            objIN = (IN) objOut;
        } else {
            objIN = mappableOf(objOut);
        }

        return ResponseEntity.ok(objIN);
    }

    public ResponseEntity<Page<IN>> findByFilter(Pageable pageable) {
        log.debug("findByFilter {}", pageable);

        return ResponseEntity.ok((Page<IN>) ConverterSpringJpa.to(
                service().findByFilter(ConverterSpringJpa.of(pageable)),
                pageable));
    }
}