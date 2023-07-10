package com.popple.server.domain.user.service;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.entity.RegisterToken;
import com.popple.server.domain.user.dto.CreateUserRequestDto;
import com.popple.server.domain.user.dto.CreateUserResponseDto;
import com.popple.server.domain.user.dto.EmailSource;
import com.popple.server.domain.user.vo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserService userService;
    private final SellerService sellerService;
    private final EmailService emailService;
    private final RegisterTokenService registerTokenService;
    private final TokenService tokenService;


    public void checkProceedEmail(String email) {
        userService.checkExistProceed(email);
    }

    public void generateRegisterTokenAndSendEmail(String email) {

        RegisterToken generateToken = registerTokenService.generateToken(email);
        EmailSource emailSource = emailService.getEmailSource(email);

        emailService.sendMail(emailSource, generateToken.getRegisterToken());
    }


    public CreateUserResponseDto register(CreateUserRequestDto createUserRequestDto) {
        CreateUserResponseDto createUserResponseDto = userService.create(createUserRequestDto);
        generateRegisterTokenAndSendEmail(createUserResponseDto.getEmail());

        return createUserResponseDto;
    }

    public String verifyRegisterToken(String registerToken) {
        return registerTokenService.verifyToken(registerToken);
    }

    public void checkDuplicationNicknameAndEmail(String nickname, String email, Role role) {
        if (nickname == null && email == null) {
            throw new RuntimeException("올바르지 않은 요청입니다.");
        }

        if (role.equals(Role.SELLER)) {
            // TODO Seller 완성하고 구현하기
            sellerService.checkDuplication(nickname, email);
            return;
        }

        userService.checkDuplication(nickname, email);
    }

    public void generateAccessAndRefreshToken(String email) {
    }
}
