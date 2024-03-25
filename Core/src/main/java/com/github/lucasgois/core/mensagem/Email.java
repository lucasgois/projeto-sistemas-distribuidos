package com.github.lucasgois.core.mensagem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Email extends Dado {

    private int id;
    private String destinatario;
    private String assunto;
    private String texto;

    public String formatar() {
        return getOrigem() + ": " + texto;
    }

}

