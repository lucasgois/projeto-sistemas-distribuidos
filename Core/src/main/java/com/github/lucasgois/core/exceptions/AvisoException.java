package com.github.lucasgois.core.exceptions;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AvisoException extends Exception {

    public AvisoException(final String message) {
        super(message);
        log.warn(message);
    }

    public AvisoException(final String message, final Throwable cause) {
        super(message, cause);
        log.warn(message, cause);
    }

    public AvisoException(final Throwable cause) {
        super(cause);
        log.warn(cause.getMessage(), cause);
    }

}
