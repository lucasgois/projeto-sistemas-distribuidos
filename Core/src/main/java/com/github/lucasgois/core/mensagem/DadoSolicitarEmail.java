package com.github.lucasgois.core.mensagem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DadoSolicitarEmail extends Dado {

    private DadoUsuario usuario;

}
