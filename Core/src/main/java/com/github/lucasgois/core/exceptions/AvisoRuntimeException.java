package com.github.lucasgois.core.exceptions;

import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class AvisoRuntimeException extends RuntimeException {

    public AvisoRuntimeException(final String message) {
        super(message);
        log.log(Level.WARNING, message);
    }

    public AvisoRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
        log.log(Level.WARNING, message, cause);
    }

    public AvisoRuntimeException(final Throwable cause) {
        super(cause);
        log.log(Level.WARNING, cause.getMessage(), cause);
    }
}
