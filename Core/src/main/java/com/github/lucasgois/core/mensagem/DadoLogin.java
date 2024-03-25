package com.github.lucasgois.core.mensagem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class DadoLogin extends Dado {

    private final String nome;
    private final String senha;

}
