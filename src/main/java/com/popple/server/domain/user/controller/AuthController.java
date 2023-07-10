package com.popple.server.domain.user.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.user.dto.*;
import com.popple.server.domain.user.service.*;
import com.popple.server.domain.user.vo.AddressStore;
import com.popple.server.domain.user.vo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/check-proceed")
    public APIDataResponse<?> checkProceedEmail(@Valid @RequestBody CheckEmailRequestDto checkEmailRequestDto) {

        authService.checkProceedEmail(checkEmailRequestDto.getEmail());

        return APIDataResponse.empty(200);
    }

    @PostMapping("/auth/regenerate-token")
    public APIDataResponse<?> regenerateRegisterToken(@Valid @RequestBody CheckEmailRequestDto checkEmailRequestDto) {
        authService.generateRegisterTokenAndSendEmail(checkEmailRequestDto.getEmail());
        return APIDataResponse.empty(200);
    }

    @PostMapping("/auth/signup")
    public APIDataResponse<?> registerUser(@RequestBody CreateUserRequestDto createUserRequestDto) {

        AddressStore.validate(createUserRequestDto.getCity(), createUserRequestDto.getDistrict());
        CreateUserResponseDto createUserResponseDto = authService.register(createUserRequestDto);
        return APIDataResponse.of(HttpStatus.OK.value(), createUserResponseDto);
    }

    //TODO TokenProvider 구현 후 완성
    @PostMapping("/auth/signin")
    public APIDataResponse<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        return null;
    }

    @PostMapping("/auth/verify-email")
    public APIDataResponse<?> verifyEmail(@RequestBody VerifyEmailRequestDto verifyEmailRequestDto) {

        String email = authService.verifyRegisterToken(verifyEmailRequestDto.getRegisterToken());
        authService.generateAccessAndRefreshToken(email);


        return APIDataResponse.empty(200);
    }


    @GetMapping("/auth/check-duplication")
    public APIDataResponse<?> duplicateNicknameAndEmail(
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "USER") Role role
    ) {

        authService.checkDuplicationNicknameAndEmail(nickname, email, role);

        return APIDataResponse.empty(200);
    }
}
