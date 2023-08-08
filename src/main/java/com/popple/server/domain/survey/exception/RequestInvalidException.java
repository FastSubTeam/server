package com.popple.server.domain.survey.exception;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
public class RequestInvalidException extends RuntimeException {
    private List<ObjectError> errors;

    public RequestInvalidException() {
    }

    public RequestInvalidException(String message) {
        super(message);
    }

    public RequestInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestInvalidException(Throwable cause) {
        super(cause);
    }

    public RequestInvalidException(String message, List<ObjectError> errors) {
        super(message);
        this.errors = errors;
    }
}
