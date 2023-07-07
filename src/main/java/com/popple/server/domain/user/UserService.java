package com.popple.server.domain.user;

import com.popple.server.domain.entity.User;
import com.popple.server.domain.user.dto.CreateUserRequestDto;
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

    @Transactional
    public CreateUserResponseDto create(final CreateUserRequestDto createUserRequestDto) {
        String encodedPassword = bCryptPasswordEncoder.encode(createUserRequestDto.getPassword());

        createUserRequestDto.setPassword(encodedPassword);
        User user = createUserRequestDto.toEntity();
        userRepository.save(user);

        return CreateUserResponseDto.from(user);
    }
}
