package com.github.lucasgois.core.mensagem;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class DadoLogin extends Dado {

    private String nome;
    private String senha;
    private UUID token;

    public DadoLogin(final UUID token) {
        this.token = token;
    }

    public DadoLogin(final String nome, final String senha) {
        this.nome = nome;
        this.senha = senha;
    }

}
