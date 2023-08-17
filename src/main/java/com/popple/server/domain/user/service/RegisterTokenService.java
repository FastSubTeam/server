package com.popple.server.domain.user.service;

import com.popple.server.domain.entity.RegisterToken;
import com.popple.server.domain.entity.Member;
import com.popple.server.domain.user.exception.AlreadyExistException;
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
            throw new RuntimeException("아직 인증이 완료되지 않은 이메일입니다.");
        }
    }

    @Transactional
    public RegisterToken generateToken(String email) {
        Member findMember = memberRepository.findByEmail(email);
        if (findMember == null) {
            throw new AlreadyExistException(UserErrorCode.NOT_FOUND);
        }

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
            throw new RuntimeException("유효하지 않은 인증토큰입니다.");
        }


        registerTokenRepository.delete(findRegisterToken);

        return findRegisterToken.getEmail();

    }
}
