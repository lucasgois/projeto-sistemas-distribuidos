package com.github.lucasgois.core.mensagem;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DadoEmail extends Dado {

    private int id;
    private String remetente;
    private String destinatario;
    private String assunto;
    private String texto;
    private List<DadoAnexo> anexos;

}
