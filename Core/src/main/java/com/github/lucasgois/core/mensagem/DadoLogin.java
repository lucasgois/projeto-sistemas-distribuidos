package com.github.lucasgois.core.mensagem;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DadoLogin extends Dado {

    private String nome;
    private String senha;
    private String token;

}
