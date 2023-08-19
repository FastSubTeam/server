package com.popple.server.domain.event.exception;

public class EventException extends RuntimeException {
    public EventException() {
    }

    public EventException(String message) {
        super(message);
    }

    public EventException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventException(Throwable cause) {
        super(cause);
    }
}
