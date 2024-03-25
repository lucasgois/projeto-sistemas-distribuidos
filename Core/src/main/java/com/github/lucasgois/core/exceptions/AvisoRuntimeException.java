package com.github.lucasgois.core.exceptions;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AvisoRuntimeException extends RuntimeException {

    public AvisoRuntimeException(final String message) {
        super(message);
        log.warn(message);
    }

    public AvisoRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
        log.warn(message, cause);
    }

    public AvisoRuntimeException(final Throwable cause) {
        super(cause);
        log.warn(cause.getMessage(), cause);
    }

}
