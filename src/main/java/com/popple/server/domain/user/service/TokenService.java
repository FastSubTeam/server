package com.popple.server.domain.user.service;

import com.popple.server.domain.user.exception.InvalidJwtTokenException;
import com.popple.server.domain.user.exception.TokenErrorCode;
import com.popple.server.domain.user.jwt.RefreshToken;
import com.popple.server.domain.user.jwt.TokenManager;
import com.popple.server.domain.user.repository.MemberRepository;
import com.popple.server.domain.user.repository.RefreshTokenRepository;
import com.popple.server.domain.user.vo.Role;
import com.popple.server.domain.user.vo.TokenPayload;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {
    private final MemberRepository memberRepository;

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenManager tokenManager;

    public String generateAccessToken(TokenPayload tokenPayload) {
        return tokenManager.generateAccessToken(tokenPayload);
    }

    public String generateAccessToken(String refreshToken) {
        if (!refreshTokenRepository.existsById(refreshToken)) {
            throw new InvalidJwtTokenException(TokenErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        Claims claims = tokenManager.parseClaims(refreshToken);

        String id = claims.get("id").toString();
        String role = claims.get("role").toString();
        TokenPayload tokenPayload = TokenPayload.builder()
                .id(Long.valueOf(id))
                .role(Role.of(role))
                .build();

        return tokenManager.generateAccessToken(tokenPayload);
    }

    public String generateRefreshToken(TokenPayload tokenPayload) {
        String encodedRefreshToken = tokenManager.generateRefreshToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.builder()
                .memberId(tokenPayload.getId())
                .role(tokenPayload.getRole())
                .token(encodedRefreshToken)
                .build();

        refreshTokenRepository.save(refreshToken);

        return encodedRefreshToken;
    }

    public void invalidateToken(String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new InvalidJwtTokenException(TokenErrorCode.INVALID_REFRESH_TOKEN));
        refreshTokenRepository.delete(findRefreshToken);
    }
}
