package com.popple.server.domain.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CreateMemberRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("비밀번호가 정규식에 부합하는 케이스")
    void passwordValidationSuccessTest() {

        CreateUserRequestDto dto = CreateUserRequestDto.builder()
                .email("qwe123@naver.com")
                .city("서울시")
                .district("강동구")
                .nickname("qwe123")
                .password("qwWer123456")
                .build();

        Set<ConstraintViolation<CreateUserRequestDto>> violations = validator.validate(dto);
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("비밀번호가 정규식에 부합하지 않는 케이스 - 글자수 미달")
    void passwordValidationFailTest() {

        CreateUserRequestDto dto = CreateUserRequestDto.builder()
                .email("qwe123@naver.com")
                .city("서울시")
                .district("강동구")
                .nickname("qwe123")
                .password("1")
                .build();

        Set<ConstraintViolation<CreateUserRequestDto>> violations = validator.validate(dto);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("닉네임이 정규식에 부합하는 케이스")
    void nicknameValidationSuccessTest() {

        CreateUserRequestDto dto = CreateUserRequestDto.builder()
                .email("qwe123@naver.com")
                .city("서울시")
                .district("강동구")
                .nickname("qwe123")
                .password("qwWer123456")
                .build();

        Set<ConstraintViolation<CreateUserRequestDto>> violations = validator.validate(dto);
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("닉네임이 정규식에 부합하지 않는 케이스 - 허용되지 않은 특수문자 포함")
    void nicknameValidationFailTest() {

        CreateUserRequestDto dto = CreateUserRequestDto.builder()
                .email("qwe123@naver.com")
                .city("서울시")
                .district("강동구")
                .nickname("qweq#23")
                .password("qwWer123456")
                .build();

        Set<ConstraintViolation<CreateUserRequestDto>> violations = validator.validate(dto);
        assertThat(violations.size()).isEqualTo(1);
    }

}