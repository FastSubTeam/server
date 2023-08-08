package com.popple.server.domain.user.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.user.annotation.LoginActor;
import com.popple.server.domain.user.dto.*;
import com.popple.server.domain.user.service.AuthService;
import com.popple.server.domain.user.service.OAuthService;
import com.popple.server.domain.user.vo.Actor;
import com.popple.server.domain.user.vo.AddressStore;
import com.popple.server.domain.user.vo.Role;
import com.popple.server.domain.user.vo.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
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


    @PostMapping("/auth/check-proceed")
    public APIDataResponse<?> checkProceedEmail(@Valid @RequestBody CheckEmailRequestDto checkEmailRequestDto) {

        authService.checkProceedEmail(checkEmailRequestDto.getEmail());

        return APIDataResponse.empty(HttpStatus.OK);
    }

    @PostMapping("/auth/regenerate-token")
    public APIDataResponse<?> regenerateRegisterToken(@Valid @RequestBody CheckEmailRequestDto checkEmailRequestDto) {
        authService.generateRegisterTokenAndSendEmail(checkEmailRequestDto.getEmail());
        return APIDataResponse.empty(HttpStatus.OK);
    }

    @PostMapping("/auth/signup")
    public APIDataResponse<?> registerUser(@RequestBody CreateUserRequestDto createUserRequestDto) {

        AddressStore.validate(createUserRequestDto.getCity(), createUserRequestDto.getDistrict());
        CreateUserResponseDto createUserResponseDto = authService.register(createUserRequestDto);
        return APIDataResponse.of(HttpStatus.OK, createUserResponseDto);
    }

    //TODO TokenProvider 구현 후 완성
    @PostMapping("/auth/signin")
    public APIDataResponse<?> login(
            @RequestBody LoginRequestDto loginRequestDto,
            @RequestParam(defaultValue = "USER") Role role
    ) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword(), role);
        return APIDataResponse.of(HttpStatus.OK, loginResponseDto);
    }

    @PostMapping("/auth/verify-email")
    public void verifyEmail(@RequestBody @Valid VerifyEmailRequestDto verifyEmailRequestDto, HttpServletResponse response) throws IOException {

        String email = authService.verifyRegisterToken(verifyEmailRequestDto.getEmail(), verifyEmailRequestDto.getRegisterToken());
        Token token = authService.generateAccessAndRefreshToken(email);

        addTokenAtCookie(response, token);


        response.sendRedirect("http://localhost:3000");
    }

    private void addTokenAtCookie(HttpServletResponse response, Token token) {
        Cookie accessTokenCookie = new Cookie("accessToken", token.getAccessToken());
        accessTokenCookie.setDomain("localhost");
        accessTokenCookie.setPath("/");
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setMaxAge(60 * 30);
        Cookie refreshTokenCookie = new Cookie("refreshToken", token.getRefreshToken());
        response.addCookie(accessTokenCookie);

        refreshTokenCookie.setDomain("localhost");
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(60 * 30);;
        response.addCookie(refreshTokenCookie);
    }


    @GetMapping("/auth/check-duplication")
    public APIDataResponse<?> duplicateNicknameAndEmail(
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "USER") Role role
    ) {

        authService.checkDuplicationNicknameAndEmail(nickname, email, role);

        return APIDataResponse.empty(HttpStatus.OK);
    }

    @GetMapping("/auth/kakaologin")
    public void kakaoSignIn(HttpServletResponse httpServletResponse) throws IOException {
        oAuthService.redirectToKakaoLoginPage(httpServletResponse);
    }

    @GetMapping("/auth/kakaologin/callback")
    public void kakaoSignInCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        LoginResponseDto loginResponseDto = oAuthService.loginWithKakaoCode(code);
        Token token = Token.builder()
                .accessToken(loginResponseDto.getAccessToken())
                .refreshToken(loginResponseDto.getRefreshToken())
                .build();

        addTokenAtCookie(response, token);
        response.sendRedirect("http://localhost:3000");
    }

}
