package org.natc.app.exception;

public class NATCException extends Exception {
    public NATCException() {
        super();
    }

    public NATCException(final String message) {
        super(message);
    }

    public NATCException(final Throwable cause) {
        super(cause);
    }

    public NATCException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
