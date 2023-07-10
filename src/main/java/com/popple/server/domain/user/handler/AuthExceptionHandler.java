package com.popple.server.domain.user.handler;

import com.popple.server.common.dto.APIErrorResponse;
import com.popple.server.domain.user.controller.AuthController;
import com.popple.server.domain.user.exception.AlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = {AuthController.class})
public class AuthExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse alreadyExistExceptionHandle(AlreadyExistException e) {
        return APIErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
