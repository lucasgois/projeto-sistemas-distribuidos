package com.github.lucasgois.core.exceptions;

public class CoreException extends Exception {

    public CoreException() {
    }

    public CoreException(final String message) {
        super(message);
    }

    public CoreException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CoreException(final Throwable cause) {
        super(cause);
    }

}
