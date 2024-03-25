package com.github.lucasgois.core.mensagem;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DadoLogin extends Dado {

    private String nome;
    private String senha;

    public DadoLogin(final String nome, final String senha) {
        this.nome = nome;
        this.senha = senha;
    }

}
