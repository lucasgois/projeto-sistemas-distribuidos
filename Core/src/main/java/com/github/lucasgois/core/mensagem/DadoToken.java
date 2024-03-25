package com.github.lucasgois.core.mensagem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class DadoToken extends Dado {

    private final UUID token;

}
