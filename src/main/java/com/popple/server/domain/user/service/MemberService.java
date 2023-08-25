package com.popple.server.domain.user.service;

import com.popple.server.domain.entity.Member;
import com.popple.server.domain.user.dto.CreateUserRequestDto;
import com.popple.server.domain.user.dto.CreateUserResponseDto;
import com.popple.server.domain.user.dto.KakaoLoginRequestDto;
import com.popple.server.domain.user.exception.AlreadyExistException;
import com.popple.server.domain.user.exception.AlreadySignUpException;
import com.popple.server.domain.user.exception.UserBadRequestException;
import com.popple.server.domain.user.exception.UserErrorCode;
import com.popple.server.domain.user.repository.MemberRepository;
import com.popple.server.domain.user.repository.RegisterTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final RegisterTokenRepository registerTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void checkExistProceed(String email) {
        if (memberRepository.existsByEmail(email) && registerTokenRepository.existsByEmail(email)) {
            throw new AlreadyExistException(UserErrorCode.PROCEEDING_EMAIL);
        }

        if (memberRepository.existsByEmail(email)) {
            throw new AlreadySignUpException(UserErrorCode.EXIST_EMAIL);
        }
    }

    @Transactional
    public Member createKakaoMember(KakaoLoginRequestDto kakaoLoginRequestDto) {
        Member kakaoMember = Member.builder()
                .email(kakaoLoginRequestDto.getEmail())
                .nickname(kakaoLoginRequestDto.getNickname())
                .password(UUID.randomUUID().toString())
                .build();

        return memberRepository.save(kakaoMember);
    }

    @Transactional
    public CreateUserResponseDto createWithPassword(final CreateUserRequestDto createUserRequestDto) {

        String email = createUserRequestDto.getEmail();
        if (memberRepository.existsByEmail(email)) {
            throw new AlreadyExistException(UserErrorCode.EXIST_EMAIL);
        }

        if (memberRepository.existsByNickname(createUserRequestDto.getNickname())) {
            throw new AlreadyExistException(UserErrorCode.EXIST_NICKNAME);
        }

        String encodedPassword = bCryptPasswordEncoder.encode(createUserRequestDto.getPassword());

        createUserRequestDto.setPassword(encodedPassword);
        Member member = createUserRequestDto.toEntity();
        memberRepository.save(member);

        return CreateUserResponseDto.from(member);
    }

    public void checkDuplication(String nickname, String email) {

        if (email != null) {
            if (memberRepository.existsByEmail(email) && registerTokenRepository.existsByEmail(email)) {
                throw new AlreadyExistException(UserErrorCode.PROCEEDING_EMAIL);
            }

            if (memberRepository.existsByEmail(email)) {
                throw new AlreadyExistException(UserErrorCode.EXIST_EMAIL);
            }
        }

        if (nickname != null) {
            if (memberRepository.existsByNickname(nickname)) {
                throw new AlreadyExistException(UserErrorCode.EXIST_NICKNAME);
            }
        }
    }

    public Member getOptionalUserByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElse(null);
    }

    public Member getUser(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UserBadRequestException(UserErrorCode.NOT_FOUND));
    }

    public Member getUser(String email, String password) {
        Member findMember = getUser(email);

        if (findMember.getInactive()) {
            throw new UserBadRequestException(UserErrorCode.NOT_FOUND);
        }

        if (!bCryptPasswordEncoder.matches(password, findMember.getPassword())) {
            throw new UserBadRequestException(UserErrorCode.INVALID_LOGIN_PAYLOAD);

        }

        return findMember;
    }

    public void updatePassword(String email, String randomPassword) {
        Member member = getUser(email);
        String encodedPassword = bCryptPasswordEncoder.encode(randomPassword);
        member.setPassword(encodedPassword);
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new UserBadRequestException(UserErrorCode.NOT_FOUND));
    }
}
