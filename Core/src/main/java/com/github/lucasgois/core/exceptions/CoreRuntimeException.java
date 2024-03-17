package com.github.lucasgois.core.exceptions;

public class CoreRuntimeException extends RuntimeException {

    public CoreRuntimeException() {
    }

    public CoreRuntimeException(final String message) {
        super(message);
    }

    public CoreRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CoreRuntimeException(final Throwable cause) {
        super(cause);
    }

}
