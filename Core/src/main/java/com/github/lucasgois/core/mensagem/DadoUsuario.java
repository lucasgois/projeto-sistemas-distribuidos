package com.github.lucasgois.core.mensagem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class DadoUsuario extends Dado {

    private String usuario;
    private String senha;

    public DadoUsuario() {
    }

    public DadoUsuario(final String usuario) {
        this.usuario = usuario;
    }

    public DadoUsuario(final String usuario, @NotNull final String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

}
