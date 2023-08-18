package com.popple.server.domain.user.handler;

import com.popple.server.common.dto.APIErrorResponse;
import com.popple.server.domain.user.controller.AuthController;
import com.popple.server.domain.user.exception.AlreadyExistException;
import com.popple.server.domain.user.exception.UserBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

@Slf4j
@RestControllerAdvice(basePackageClasses = {AuthController.class})
public class AuthExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse constraintViolationExceptionHandle(ConstraintViolationException e) {
        return APIErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {

        // TODO 수정 필요, APIErrorResponse를 바꾸기
        return APIErrorResponse.of(HttpStatus.BAD_REQUEST, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse bindExceptionHandle(BindException e) {

        return APIErrorResponse.of(HttpStatus.BAD_REQUEST, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse userBusinessExceptionHandle(UserBusinessException e) {
        return APIErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse alreadyExistExceptionHandle(AlreadyExistException e) {
        return APIErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIErrorResponse unknownExceptionHandle(RuntimeException e) {
        log.error(e.toString());
        return APIErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

}
