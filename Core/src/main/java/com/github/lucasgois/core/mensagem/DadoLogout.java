package com.github.lucasgois.core.mensagem;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class DadoLogout extends Dado {

    private UUID token;

    public DadoLogout(final UUID token) {
        this.token = token;
    }

}
