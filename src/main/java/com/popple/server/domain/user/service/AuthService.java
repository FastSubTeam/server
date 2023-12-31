package com.popple.server.domain.user.service;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.entity.*;
import com.popple.server.domain.user.dto.*;
import com.popple.server.domain.user.exception.InvalidRequestParameterException;
import com.popple.server.domain.user.exception.UserBadRequestException;
import com.popple.server.domain.user.exception.UserErrorCode;
import com.popple.server.domain.user.vo.EmailMessage;
import com.popple.server.domain.user.vo.Role;
import com.popple.server.domain.user.vo.Token;
import com.popple.server.domain.user.vo.TokenPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberService memberService;
    private final SellerService sellerService;
    private final EmailService emailService;
    private final RegisterTokenService registerTokenService;
    private final TokenService tokenService;
    private final AdminService adminService;

    private final RandomPasswordGenerator randomPasswordGenerator;

    public String reIssueAccessToken(String refreshToken) {
        return tokenService.generateAccessToken(refreshToken);
    }

    public void checkProceedEmail(String email) {
        memberService.checkExistProceed(email);
    }

    public RegisterToken generateRegisterTokenAndSendEmail(String email) {

        RegisterToken generateToken = registerTokenService.generateToken(email);
        EmailSource emailSource = emailService.getEmailSource(email, EmailMessage.REGISTER_SUBJECT);

        emailService.sendRegisterMail(emailSource, generateToken.getRegisterToken());

        return generateToken;
    }

    public void generateRandomPassword(String email) {
        EmailSource emailSource = emailService.getEmailSource(email, EmailMessage.RANDOM_PASSWORD_SUBJECT);
        String randomPassword = randomPasswordGenerator.generateRandomPassword(10);
        System.out.println(randomPassword);
        emailService.sendRandomPasswordMail(emailSource, randomPassword);

        memberService.updatePassword(email, randomPassword);
    }


    public CreateUserResponseDto registerMember(CreateUserRequestDto createUserRequestDto) {
        CreateUserResponseDto createUserResponseDto = memberService.createWithPassword(createUserRequestDto);
        RegisterToken registerToken = generateRegisterTokenAndSendEmail(createUserResponseDto.getEmail());

        // =============== TODO 배포시에 지우기, 메소드 시그니처 void로 수정 ==================
        createUserResponseDto.setRegisterToken(registerToken.getRegisterToken());
        // =====================================================

        return createUserResponseDto;
    }

    public void registerSeller(CreateSellerRequestDto createSellerRequestDto) {
        sellerService.checkDuplication(createSellerRequestDto.getNickname(), createSellerRequestDto.getEmail(), createSellerRequestDto.getBusinessNumber());
        sellerService.create(createSellerRequestDto);
    }

    public String verifyRegisterToken(String email, String registerToken) {
        return registerTokenService.verifyToken(email, registerToken);
    }

    public void checkDuplicationNicknameAndEmail(String nickname, String email, Role role) {
        if (nickname == null && email == null) {
            throw new InvalidRequestParameterException(UserErrorCode.INVALID_CHECK_DUPLICATION_PARAMETER);
        }

        if (role.equals(Role.ROLE_SELLER)) {
            sellerService.validateDuplicatedEmail(email);
            sellerService.validateDuplicatedNickname(nickname);
            return;
        }

        memberService.checkDuplication(nickname, email);
    }

    public Token generateAccessAndRefreshToken(String email) {

        Member member = memberService.getUser(email);
        TokenPayload tokenPayload = member.toPayload();
        String accessToken = tokenService.generateAccessToken(tokenPayload);
        String refreshToken = tokenService.generateRefreshToken(tokenPayload);

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    public LoginResponseDto login(String email, String password, Role role) {

        if (role.equals(Role.ROLE_USER)) {

            registerTokenService.checkRegisteredEmail(email);

            Member member = memberService.getUser(email, password);

            TokenPayload tokenPayload = member.toPayload();
            String accessToken = tokenService.generateAccessToken(tokenPayload);
            String refreshToken = tokenService.generateRefreshToken(tokenPayload);

            return LoginResponseDto.builder()
                    .userId(member.getId())
                    .role(role)
                    .platform(LoginPlatform.BASIC)
                    .email(member.getEmail())
                    .profileImgUrl(member.getProfileImgUrl())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .nickname(member.getNickname())
                    .build();
        }

        Seller seller = sellerService.getUser(email, password);
        TokenPayload tokenPayload = seller.toPayload();
        String accessToken = tokenService.generateAccessToken(tokenPayload);
        String refreshToken = tokenService.generateRefreshToken(tokenPayload);
        return LoginResponseDto.builder()
                .userId(seller.getId())
                .role(role)
                .email(seller.getEmail())
                .platform(LoginPlatform.BASIC)
                .profileImgUrl(seller.getProfileImgUrl())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(seller.getNickname())
                .build();
    }

    public APIDataResponse<?> verifyBusinessNumber(ValidateBusinessNumberRequestDto validateBusinessNumberRequestDto) throws IOException {
        return sellerService.checkExistBusinessNumberWithOpenAPI(validateBusinessNumberRequestDto);
    }

    public void logout(String refreshToken) {
        tokenService.invalidateToken(refreshToken);
    }

    public boolean isAdmin(LoginRequestDto loginRequestDto) {
        return adminService.checkAdmin(loginRequestDto);
    }

    public LoginResponseDto adminLogin(LoginRequestDto loginRequestDto) {
        Admin admin = adminService.getAdmin(loginRequestDto.getEmail());
        TokenPayload tokenPayload = admin.toPayload();
        String accessToken = tokenService.generateAccessToken(tokenPayload);
        String refreshToken = tokenService.generateRefreshToken(tokenPayload);
        return LoginResponseDto.builder()
                .userId(Long.valueOf(admin.getId()))
                .role(tokenPayload.getRole())
                .email(admin.getEmail())
                .profileImgUrl("관리자는 사진따위 없음")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname("관리자")
                .build();
    }

    public void checkDuplicatedBusinessNumber(String businessNumber) {
        sellerService.validateDuplicatedBusinessNumber(businessNumber);
    }

    public void checkBusinessNumberValidity(String businessNumber) throws IOException {
        sellerService.checkBusinessNumberValidity(businessNumber);
    }

    public MemberProfileResponseDto getMemberProfile(Long id) {
        Member member = memberService.getMemberById(id);
        return MemberProfileResponseDto.builder()
                .nickname(member.getNickname())
                .city(member.getCity())
                .district(member.getDistrict())
                .profileImgUrl(member.getProfileImgUrl())
                .build();
    }

    public SellerProfileResponseDto getSellerProfile(Long id) {
        Seller seller = sellerService.getSellerById(id);
        return SellerProfileResponseDto.builder()
                .bio(seller.getBio())
                .address(seller.getAddress())
                .shopName(seller.getShopName())
                .profileImgUrl(seller.getProfileImgUrl())
                .nickname(seller.getNickname())
                .build();
    }

    public void updateMemberProfile(Long id, UpdateMemberProfileRequestDto updateMemberProfileRequestDto) {
        Member member = memberService.getMemberById(id);
        member.updateProfile(updateMemberProfileRequestDto);
    }

    public void verifyRoadAddress(String address) {
        sellerService.verifyRoadAddress(address);
    }

    public void updateSellerProfile(Long id, UpdateSellerProfileRequestDto updateSellerProfileRequestDto) {
        Seller seller = sellerService.getSellerById(id);
        seller.updateProfile(updateSellerProfileRequestDto);
    }

    public void updatePassword(Long id, Role role, String password) {
        if (role.equals(Role.ROLE_USER)) {
            Member member = memberService.getMemberById(id);
            String newPassword = bCryptPasswordEncoder.encode(password);
            member.updatePassword(newPassword);
            return;
        }

        Seller seller = sellerService.getSellerById(id);
        String newPassword = bCryptPasswordEncoder.encode(password);
        seller.updatePassword(newPassword);
    }

    public void removeActor(Long id, Role role, RemoveActorRequestDto removeActorRequestDto) {
        if (role.equals(Role.ROLE_USER)) {
            Member member = memberService.getMemberById(id);

            if (member.getEmail().equals(removeActorRequestDto.getEmail())
                    && bCryptPasswordEncoder.matches(removeActorRequestDto.getPassword(), member.getPassword())) {
                member.setInactive();
                return;
            }
            throw new UserBadRequestException(UserErrorCode.INVALID_INPUT);
        }

        Seller seller = sellerService.getSellerById(id);
        if (seller.getEmail().equals(removeActorRequestDto.getEmail())
                && bCryptPasswordEncoder.matches(removeActorRequestDto.getPassword(), seller.getPassword())) {
            sellerService.removeById(id);
            return;
        }
        throw new UserBadRequestException(UserErrorCode.INVALID_INPUT);
    }
}
