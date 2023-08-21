package com.popple.server.domain.event.exception;

import com.popple.server.common.dto.APIErrorResponse;
import com.popple.server.domain.event.controller.EventController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = { EventController.class })
public class EventExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public APIErrorResponse eventExceptionHandler(EventException exception) {
        return APIErrorResponse.of(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public APIErrorResponse exceptionHandler(Exception exception) {
        exception.printStackTrace();
        return APIErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}
