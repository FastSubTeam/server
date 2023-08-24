package com.popple.server.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.entity.RegisterToken;
import com.popple.server.domain.user.annotation.LoginActor;
import com.popple.server.domain.user.dto.*;
import com.popple.server.domain.user.exception.UserErrorCode;
import com.popple.server.domain.user.exception.UserUnauthorizedException;
import com.popple.server.domain.user.service.AuthService;
import com.popple.server.domain.user.service.OAuthService;
import com.popple.server.domain.user.vo.Actor;
import com.popple.server.domain.user.vo.AddressStore;
import com.popple.server.domain.user.vo.Fetch;
import com.popple.server.domain.user.vo.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {


    private final AuthService authService;
    private final OAuthService oAuthService;

    @GetMapping("/test")
    public Actor test(@LoginActor Actor actor) {
        return actor;
    }

    @GetMapping("/profile")
    public APIDataResponse<?> getLoginMemberProfile(@LoginActor Actor actor) {
        MemberProfileResponseDto memberProfile = authService.getMemberProfile(actor.getId());
        return APIDataResponse.of(HttpStatus.OK, memberProfile);
    }

    @PutMapping("/profile")
    public APIDataResponse<?> updateLoginMemberProfile(
            @LoginActor Actor actor,
            @RequestBody @Valid UpdateMemberProfileRequestDto updateMemberProfileRequestDto
    ) {
        AddressStore.validate(updateMemberProfileRequestDto.getCity(), updateMemberProfileRequestDto.getDistrict());
        authService.updateMemberProfile(actor.getId(), updateMemberProfileRequestDto);

        return APIDataResponse.empty(HttpStatus.OK);
    }

    @GetMapping("/profile/seller")
    public APIDataResponse<?> getLoginSellerProfile(@LoginActor Actor actor) {
        SellerProfileResponseDto sellerProfile = authService.getSellerProfile(actor.getId());
        return APIDataResponse.of(HttpStatus.OK, sellerProfile);
    }

    @PutMapping("/profile/seller")
    public APIDataResponse<?> updateLoginSellerProfile(
            @LoginActor Actor actor,
            @RequestBody @Valid UpdateSellerProfileRequestDto updateSellerProfileRequestDto
    ) {
        authService.verifyRoadAddress(updateSellerProfileRequestDto.getAddress());
        authService.updateSellerProfile(actor.getId(), updateSellerProfileRequestDto);
        return APIDataResponse.empty(HttpStatus.OK);
    }

    @GetMapping("/auth/reissue")
    public APIDataResponse<?> reIssueAccessToken(@RequestHeader("RefreshToken") String refreshToken) {
        if (refreshToken == null) {
            throw new UserUnauthorizedException(UserErrorCode.NEED_REFRESH_TOKEN);
        }
        String accessToken = authService.reIssueAccessToken(refreshToken);
        ReissueAccessTokenRequestDto response = ReissueAccessTokenRequestDto.builder()
                .accessToken(accessToken)
                .build();

        return APIDataResponse.of(HttpStatus.OK, response);
    }

    @PatchMapping("/auth/password")
    public APIDataResponse<?> updatePassword(@LoginActor Actor actor, @RequestBody @Valid UpdatePasswordRequestDto updatePasswordRequestDto) {
        authService.updatePassword(actor.getId(), actor.getRole(), updatePasswordRequestDto.getPassword());
        return APIDataResponse.empty(HttpStatus.OK);
    }


    @PostMapping("/auth/check-proceed")
    public APIDataResponse<?> checkProceedEmail(@Valid @RequestBody CheckEmailRequestDto checkEmailRequestDto) {

        authService.checkProceedEmail(checkEmailRequestDto.getEmail());

        return APIDataResponse.empty(HttpStatus.OK);
    }

    @PostMapping("/auth/seller/check-businessNumber")
    public APIDataResponse<?> checkDuplicatedBusinessNumber(@RequestBody @Valid CheckDuplicatedBusinessNumberRequestDto checkDuplicatedBusinessNumberRequestDto) {
        authService.checkDuplicatedBusinessNumber(checkDuplicatedBusinessNumberRequestDto.getBusinessNumber());
        return APIDataResponse.empty(HttpStatus.OK);
    }

    @PostMapping("/auth/regenerate-token")
    public APIDataResponse<?> regenerateRegisterToken(@Valid @RequestBody CheckEmailRequestDto checkEmailRequestDto) {
        RegisterToken registerToken = authService.generateRegisterTokenAndSendEmail(checkEmailRequestDto.getEmail());
        return APIDataResponse.of(HttpStatus.OK, registerToken.getRegisterToken());
    }

    @PostMapping("/auth/forgotpassword")
    public APIDataResponse<?> generateRandomPassword(@Valid @RequestBody FindPasswordRequestDto findPasswordRequestDto) {
        authService.generateRandomPassword(findPasswordRequestDto.getEmail());

        return APIDataResponse.empty(HttpStatus.OK);
    }

    @PostMapping("/auth/signup")
    public APIDataResponse<?> registerUser(@RequestBody CreateUserRequestDto createUserRequestDto) {

        AddressStore.validate(createUserRequestDto.getCity(), createUserRequestDto.getDistrict());

        // =============== TODO 배포시에 void로 수정 ==================
        CreateUserResponseDto createUserResponseDto = authService.registerMember(createUserRequestDto);
        // ==========================================================
        return APIDataResponse.of(HttpStatus.OK, createUserResponseDto);
    }

    @PostMapping("/auth/signup/seller")
    public APIDataResponse<?> registerSeller(@RequestBody @Valid CreateSellerRequestDto createSellerRequestDto) throws IOException {
        authService.checkBusinessNumberValidity(createSellerRequestDto.getBusinessNumber());
        authService.registerSeller(createSellerRequestDto);
        return APIDataResponse.empty(HttpStatus.CREATED);
    }

    @GetMapping("/auth/logout")
    public APIDataResponse<?> logout(@RequestHeader(name = "RefreshToken") String refreshToken) {
        authService.logout(refreshToken);
        return APIDataResponse.empty(HttpStatus.OK);
    }

    @PostMapping("/auth/validate-business-number")
    public APIDataResponse<?> validateBusinessNumber(@RequestBody @Valid ValidateBusinessNumberRequestDto validateBusinessNumberRequestDto) throws IOException {
        return authService.verifyBusinessNumber(validateBusinessNumberRequestDto);
    }

    //TODO TokenProvider 구현 후 완성
    @PostMapping("/auth/signin")
    public APIDataResponse<?> login(
            @RequestBody LoginRequestDto loginRequestDto,
            @RequestParam(defaultValue = "USER") Role role
    ) {
        if (role.equals(Role.ROLE_USER) && authService.isAdmin(loginRequestDto)) {
            LoginResponseDto loginResponseDto = authService.adminLogin(loginRequestDto);
            return APIDataResponse.of(HttpStatus.OK, loginResponseDto);
        }
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword(), role);
        return APIDataResponse.of(HttpStatus.OK, loginResponseDto);
    }

    @PostMapping("/auth/verify-email")
    public void verifyEmail(@RequestBody @Valid VerifyEmailRequestDto verifyEmailRequestDto, HttpServletResponse response) throws IOException {

        String email = authService.verifyRegisterToken(verifyEmailRequestDto.getEmail(), verifyEmailRequestDto.getRegisterToken());
//        Token token = authService.generateAccessAndRefreshToken(email);

//        addTokenAtCookie(response, token);


//        response.sendRedirect("http://localhost:3000");
    }

    @GetMapping("/auth/check-duplication")
    public APIDataResponse<?> duplicateNicknameAndEmail(
            @ModelAttribute @Valid CheckValidateRequestDto checkValidateRequestDto,
            @RequestParam(defaultValue = "USER") Role role
    ) {

        authService.checkDuplicationNicknameAndEmail(checkValidateRequestDto.getNickname(), checkValidateRequestDto.getEmail(), role);

        return APIDataResponse.empty(HttpStatus.OK);
    }

    @GetMapping("/auth/kakaologin")
    public void kakaoSignIn(HttpServletResponse httpServletResponse) throws IOException {
        oAuthService.redirectToKakaoLoginPage(httpServletResponse);
    }

    @PostMapping("/auth/kakaologin")
    public APIDataResponse<?> kakaologin(@RequestBody KakaoLoginAccessTokenRequestDto kakaoLoginAccessTokenRequestDto) throws JsonProcessingException {
        LoginResponseDto loginResponseDto = oAuthService.loginWithKakaoAccessToken(kakaoLoginAccessTokenRequestDto.getAccessToken());
        return APIDataResponse.of(HttpStatus.OK, loginResponseDto);
    }

//    @GetMapping("/auth/kakaologin/callback")
//    public void kakaoSignInCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
//        LoginResponseDto loginResponseDto = oAuthService.loginWithKakaoCode(code);
//        Token token = Token.builder()
//                .accessToken(loginResponseDto.getAccessToken())
//                .refreshToken(loginResponseDto.getRefreshToken())
//                .build();
//
//        addTokenAtCookie(response, token);
//        response.sendRedirect("http://localhost:3000");
//    }
//
//    private void addTokenAtCookie(HttpServletResponse response, Token token) {
//        Cookie accessTokenCookie = new Cookie("accessToken", token.getAccessToken());
//        accessTokenCookie.setDomain("localhost");
//        accessTokenCookie.setPath("/");
//        accessTokenCookie.setSecure(true);
//        accessTokenCookie.setMaxAge(60 * 30);
//        Cookie refreshTokenCookie = new Cookie("refreshToken", token.getRefreshToken());
//        response.addCookie(accessTokenCookie);
//
//        refreshTokenCookie.setDomain("localhost");
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setSecure(true);
//        refreshTokenCookie.setMaxAge(60 * 30);
//        response.addCookie(refreshTokenCookie);
//    }
}
