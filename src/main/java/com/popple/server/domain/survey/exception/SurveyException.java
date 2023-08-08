package com.popple.server.domain.survey.exception;

public class SurveyException extends RuntimeException {
    public SurveyException() {
    }

    public SurveyException(String message) {
        super(message);
    }

    public SurveyException(String message, Throwable cause) {
        super(message, cause);
    }

    public SurveyException(Throwable cause) {
        super(cause);
    }
}
