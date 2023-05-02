package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.service.Service;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class DeleteControllerImpl<
        IN extends Identifiable<ID>,
        OUT extends Identifiable<ID>,
        S extends Service<OUT> & Deletable<OUT>,
        ID extends Serializable>
        implements
        Controller<IN, OUT, S> {

    private final S service;
    private final Class<OUT> outClass;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public ResponseEntity<Object> delete(ID id) {
        log.debug("delete {}", id);

        service().delete(Identifiable.of(outClass, id));
        return ResponseEntity.ok().build();
    }
}