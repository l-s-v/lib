package com.lsv.lib.mapping.modelmapper.test;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Source {
    private UUID id;
    private String nome;
    private String descricao;
}
