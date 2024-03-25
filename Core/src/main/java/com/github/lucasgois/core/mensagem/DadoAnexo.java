package com.github.lucasgois.core.mensagem;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DadoAnexo extends Dado {

    private String nomeAnexo;
    private byte[] anexo;

}
