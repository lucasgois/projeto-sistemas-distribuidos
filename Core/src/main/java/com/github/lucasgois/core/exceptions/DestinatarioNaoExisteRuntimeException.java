package com.github.lucasgois.core.exceptions;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DestinatarioNaoExisteRuntimeException extends RuntimeException {

    public DestinatarioNaoExisteRuntimeException() {
        log.error("DestinatarioNaoExisteRuntimeException");
    }

}
