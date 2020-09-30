package org.natc.app.exception;

public class NATCException extends Exception {
    public NATCException() {
        super();
    }

    public NATCException(final Exception ex) {
        super(ex);
    }
}
