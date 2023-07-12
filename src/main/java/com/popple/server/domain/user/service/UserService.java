package com.popple.server.domain.user.service;

import com.popple.server.domain.entity.Member;
import com.popple.server.domain.user.repository.UserRepository;
import com.popple.server.domain.user.dto.CreateUserRequestDto;
import com.popple.server.domain.user.dto.CreateUserResponseDto;
import com.popple.server.domain.user.exception.AlreadyExistException;
import com.popple.server.domain.user.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void checkExistProceed(String email) {
        Member findMember = userRepository.findByEmail(email);

        if (findMember != null) {
            throw new AlreadyExistException(UserErrorCode.PROCEEDING_EMAIL);
        }
    }

    @Transactional
    public CreateUserResponseDto create(final CreateUserRequestDto createUserRequestDto) {

        String email = createUserRequestDto.getEmail();
        Member findMember = userRepository.findByEmail(email);
        if (findMember != null) {
            throw new AlreadyExistException(UserErrorCode.EXIST_EMAIL);
        }

        String encodedPassword = bCryptPasswordEncoder.encode(createUserRequestDto.getPassword());

        createUserRequestDto.setPassword(encodedPassword);
        Member member = createUserRequestDto.toEntity();
        userRepository.save(member);

        return CreateUserResponseDto.from(member);
    }

    public void checkDuplication(String nickname, String email) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException();
        }

        if (userRepository.existsByNickname(nickname)) {
            throw new RuntimeException();
        }
    }

    public Member getUser(String email) {
        Member findMember = userRepository.findByEmail(email);

        if (findMember == null) {
            throw new RuntimeException();
        }

        return findMember;
    }

    public Member getUser(String email, String password) {
        Member findMember = userRepository.findByEmail(email);

        if (findMember == null) {
            throw new RuntimeException();
        }

        if (!bCryptPasswordEncoder.matches(password, findMember.getPassword())) {
            throw new RuntimeException();
        }

        return findMember;
    }
}
