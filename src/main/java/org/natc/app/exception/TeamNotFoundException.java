package org.natc.app.exception;

public class TeamNotFoundException extends NATCException {
    public TeamNotFoundException() {
        super();
    }

    public TeamNotFoundException(final String message) {
        super(message);
    }

    public TeamNotFoundException(final Throwable cause) {
        super(cause);
    }

    public TeamNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
