package com.popple.server.domain.board.exception;

import com.popple.server.common.dto.APIErrorResponse;
import com.popple.server.domain.board.controller.BoardController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice(basePackageClasses= BoardController.class)
public class BoardExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public APIErrorResponse handleUnAuthorizedException(UnauthorizedAccessException e){
        return APIErrorResponse.of(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public APIErrorResponse handleUsernameNotFoundException(UsernameNotFoundException e){
        return APIErrorResponse.of(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse handleInvalidRequestParameterException(InvalidBoardParameterException e){
        return APIErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIErrorResponse handleEntityNotFoundException(EntityNotFoundException e){
        return APIErrorResponse.of(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
