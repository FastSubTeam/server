package com.popple.server.domain.user.service;

import com.popple.server.domain.user.jwt.RefreshToken;
import com.popple.server.domain.user.jwt.TokenManager;
import com.popple.server.domain.user.repository.MemberRepository;
import com.popple.server.domain.user.repository.RefreshTokenRepository;
import com.popple.server.domain.user.vo.TokenPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenManager tokenManager;

    public String generateAccessToken(TokenPayload tokenPayload) {
        return tokenManager.generateAccessToken(tokenPayload);
    }

    public String generateRefreshToken(TokenPayload tokenPayload) {
        String encodedRefreshToken = tokenManager.generateRefreshToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.builder()
                .memberId(tokenPayload.getId())
                .role(tokenPayload.getRole())
                .refreshToken(encodedRefreshToken)
                .build();

        refreshTokenRepository.save(refreshToken);

        return encodedRefreshToken;
    }
}
