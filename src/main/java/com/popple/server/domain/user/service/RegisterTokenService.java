package com.popple.server.domain.user.service;

import com.popple.server.domain.entity.RegisterToken;
import com.popple.server.domain.entity.Member;
import com.popple.server.domain.user.exception.AlreadyExistException;
import com.popple.server.domain.user.exception.UserBusinessException;
import com.popple.server.domain.user.exception.UserErrorCode;
import com.popple.server.domain.user.repository.RegisterTokenRepository;
import com.popple.server.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegisterTokenService {

    private final MemberRepository memberRepository;
    private final RegisterTokenRepository registerTokenRepository;

    public void checkRegisteredEmail(String email) {
        RegisterToken findRegisterToken = registerTokenRepository.findByEmail(email);

        if (findRegisterToken != null) {
            throw new UserBusinessException(UserErrorCode.NOT_PROCEEDING_REGISTERED_EMAIL);
        }
    }

    @Transactional
    public RegisterToken generateToken(String email) {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AlreadyExistException(UserErrorCode.NOT_FOUND));

        RegisterToken findRegisterToken = registerTokenRepository.findByEmail(email);
        if (findRegisterToken != null) {
            registerTokenRepository.delete(findRegisterToken);
            // TODO if문 걸리는데 커밋 될 때 delete문이 안나감 flush 안쓰고 어케 해결?
            registerTokenRepository.flush();
        }

        RegisterToken newRegisterToken = RegisterToken.of(email);
        return registerTokenRepository.save(newRegisterToken);
    }

    @Transactional
    public String verifyToken(String email, String registerToken) {
        RegisterToken findRegisterToken = registerTokenRepository.findByEmailAndRegisterToken(email, registerToken);
        if (findRegisterToken == null) {
            throw new UserBusinessException(UserErrorCode.NOT_FOUND_REGISTER_TOKEN);
        }


        registerTokenRepository.delete(findRegisterToken);

        return findRegisterToken.getEmail();

    }
}
