package com.github.lucasgois.core.exceptions;

import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class ErroRuntimeException extends RuntimeException {

    public ErroRuntimeException(final String message) {
        super(message);
        log.log(Level.SEVERE, message);
    }

    public ErroRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
        log.log(Level.SEVERE, message, cause);
    }

    public ErroRuntimeException(final Throwable cause) {
        super(cause);
        log.log(Level.SEVERE, cause.getMessage(), cause);
    }
}
