package org.natc.app.exception;

public class TeamManagerDraftException extends  NATCException {
    public TeamManagerDraftException() {
        super();
    }

    public TeamManagerDraftException(final String message) {
        super(message);
    }

    public TeamManagerDraftException(final Throwable cause) {
        super(cause);
    }

    public TeamManagerDraftException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
