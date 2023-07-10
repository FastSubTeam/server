package com.popple.server.domain.user.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.user.dto.*;
import com.popple.server.domain.user.service.*;
import com.popple.server.domain.user.vo.AddressStore;
import com.popple.server.domain.user.vo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
}
