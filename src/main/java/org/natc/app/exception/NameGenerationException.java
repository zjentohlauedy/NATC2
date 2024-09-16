package org.natc.app.exception;

public class NameGenerationException extends NATCException {
    public NameGenerationException() {
        super();
    }

    public NameGenerationException(final String message) {
        super(message);
    }

    public NameGenerationException(final Throwable cause) {
        super(cause);
    }

    public NameGenerationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
