package com.github.lucasgois.core.mensagem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class DadoConexao extends Dado {

    private final boolean status;

    public static final DadoConexao ONLINE = new DadoConexao(true);
    public static final DadoConexao OFFLINE = new DadoConexao(false);

}
