package com.github.lucasgois.core.exceptions;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ErroRuntimeException extends RuntimeException {

    public ErroRuntimeException(final String message) {
        super(message);
        log.error(message);
    }

    public ErroRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
        log.error(message, cause);
    }

    public ErroRuntimeException(final Throwable cause) {
        super(cause);
        log.error(cause.getMessage(), cause);
    }

}
