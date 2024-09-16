package org.natc.app.exception;

public class LeagueProcessingException extends NATCException {
    public LeagueProcessingException() {
        super();
    }

    public LeagueProcessingException(final String message) {
        super(message);
    }

    public LeagueProcessingException(final Throwable cause) {
        super(cause);
    }

    public LeagueProcessingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
