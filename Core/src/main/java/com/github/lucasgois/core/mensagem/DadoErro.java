package com.github.lucasgois.core.mensagem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DadoErro extends Dado {

    private String mensagem;

}
