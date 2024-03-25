package com.github.lucasgois.core.exceptions;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ErroException extends Exception {

    public ErroException(final String message) {
        super(message);
        log.error(message);
    }

    public ErroException(final String message, final Throwable cause) {
        super(message, cause);
        log.error(message, cause);
    }

    public ErroException(final Throwable cause) {
        super(cause);
        log.error(cause.getMessage(), cause);
    }

}
