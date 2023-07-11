package com.popple.server.domain.user.service;

import com.popple.server.domain.entity.RegisterToken;
import com.popple.server.domain.entity.User;
import com.popple.server.domain.user.exception.AlreadyExistException;
import com.popple.server.domain.user.exception.UserErrorCode;
import com.popple.server.domain.user.repository.RegisterTokenRepository;
import com.popple.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegisterTokenService {

    private final UserRepository userRepository;
    private final RegisterTokenRepository registerTokenRepository;

    @Transactional
    public RegisterToken generateToken(String email) {
        User findUser = userRepository.findByEmail(email);
        if (findUser == null) {
            throw new AlreadyExistException(UserErrorCode.NOT_FOUND);
        }

        RegisterToken findRegisterToken = registerTokenRepository.findByEmail(email);
        if (findRegisterToken != null) {
            registerTokenRepository.delete(findRegisterToken);
        }

        RegisterToken newRegisterToken = RegisterToken.of(email);
        return registerTokenRepository.save(newRegisterToken);
    }

    @Transactional
    public String verifyToken(String email, String registerToken) {
        RegisterToken findRegisterToken = registerTokenRepository.findByEmailAndRegisterToken(email, registerToken);
        if (findRegisterToken == null) {
            throw new RuntimeException("유효하지 않은 인증토큰입니다.");
        }


        registerTokenRepository.delete(findRegisterToken);

        return findRegisterToken.getEmail();

    }
}
