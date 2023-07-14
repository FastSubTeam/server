package com.popple.server.domain.user;

import com.popple.server.domain.entity.Member;
import com.popple.server.domain.user.dto.CreateUserRequestDto;
import com.popple.server.domain.user.dto.CreateUserResponseDto;
import com.popple.server.domain.user.exception.AlreadyExistException;
import com.popple.server.domain.user.exception.UserErrorCode;
import com.popple.server.domain.user.repository.MemberRepository;
import com.popple.server.domain.user.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private static final CreateUserRequestDto CREATE_USER_REQUEST_DTO = CreateUserRequestDto.builder()
            .city("서울특별시")
            .district("강동구")
            .nickname("TTAETTAE")
            .password("1234")
            .email("qwer1234@naver.com")
            .build();

    private static final CreateUserResponseDto CREATE_USER_RESPONSE_DTO = CreateUserResponseDto.builder()
            .id(1L)
            .nickname("TTAETTAE")
            .email("qwer1234@namver.com")
            .city("서울특별시")
            .district("강동구")
            .build();
    @Spy
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Test
    @DisplayName("유저 생성 테스트")
    void createUserTest() {
        // Given
        Member member = CREATE_USER_REQUEST_DTO.toEntity();
        member.setId(1L);

        // When
        memberService.createWithPassword(CREATE_USER_REQUEST_DTO);

        // Then
        then(memberRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("유저 생성 예외 테스트 - 이미 존재하는 이메일")
    void createUserThrowAlreadyExistExceptionTest() {
        // Given
        Member member = CREATE_USER_REQUEST_DTO.toEntity();
        member.setId(1L);
        given(memberRepository.findByEmail(anyString())).willReturn(member);

        // When
        Throwable throwable = catchThrowable(() -> memberService.createWithPassword(CREATE_USER_REQUEST_DTO));

        // Then
        assertThat(throwable)
                .isInstanceOf(AlreadyExistException.class)
                .hasMessage(UserErrorCode.EXIST_EMAIL.getErrorMessage());

    }


}