package com.popple.server.domain.survey.exception;

import com.popple.server.common.dto.APIErrorResponse;
import com.popple.server.domain.survey.controller.SurveyAdminController;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = SurveyAdminController.class)
public class SurveyAdminExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public APIErrorResponse handleValidationException(RequestInvalidException e) {
        // TODO: 유효성 검사에서 여러 필드가 실패했을 경우 메시지가 여러개일 가능성이 생기는 부분 의논
        return APIErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public APIErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return APIErrorResponse.of(HttpStatus.BAD_REQUEST, "올바르지 않은 Body 데이터입니다.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public APIErrorResponse handleAllException(Exception e) {
        System.out.println(">>> " + e.getClass() + " ==> " + e.getMessage());
        return APIErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
    }
}
