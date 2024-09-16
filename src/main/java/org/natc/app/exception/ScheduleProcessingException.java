package org.natc.app.exception;

public class ScheduleProcessingException extends NATCException {
    public ScheduleProcessingException() {
        super();
    }

    public ScheduleProcessingException(final String message) {
        super(message);
    }

    public ScheduleProcessingException(final Throwable cause) {
        super(cause);
    }

    public ScheduleProcessingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
