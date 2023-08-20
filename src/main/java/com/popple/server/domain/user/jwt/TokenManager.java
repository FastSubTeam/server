package com.popple.server.domain.user.jwt;

import com.popple.server.domain.user.exception.InvalidJwtTokenException;
import com.popple.server.domain.user.exception.TokenErrorCode;
import com.popple.server.domain.user.repository.RefreshTokenRepository;
import com.popple.server.domain.user.vo.Role;
import com.popple.server.domain.user.vo.Token;
import com.popple.server.domain.user.vo.TokenPayload;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class TokenManager {
    private static final String ACCESS_TOKEN = "Authorization";
    private static final String REFRESH_TOKEN = "RefreshToken";
    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;

    private final Long accessTokenExpires;
    private final Long refreshTokenExpires;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenManager(
            @Value("${jwt.access-secret}") String accessSecretKey,
            @Value("${jwt.refresh-secret}") String refreshSecretKey,
            @Value("${jwt.access-token-expires}") String accessTokenExpires,
            @Value("${jwt.refresh-token-expires}") String refreshTokenExpires,
            UserDetailsService userDetailsService,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.accessSecretKey = Keys.hmacShaKeyFor(accessSecretKey.getBytes(StandardCharsets.UTF_8));
        this.refreshSecretKey = Keys.hmacShaKeyFor(accessSecretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpires = Long.parseLong(accessTokenExpires);
        this.refreshTokenExpires = Long.parseLong(refreshTokenExpires);
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        GrantedAuthority authority = new SimpleGrantedAuthority(claims.get("role").toString());

        UserDetails principal = userDetailsService.loadUserByUsername(claims.get("id").toString());

        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());

    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(accessSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new InvalidJwtTokenException(TokenErrorCode.EXPIRED_ACCESS_TOKEN);
        }
    }
    public boolean validateAccessToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(accessSecretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            log.info("Invalid JWT Signature", e);
            throw new InvalidJwtTokenException(TokenErrorCode.INVALID_SIGNATURE_IN_ACCESS_TOKEN);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new InvalidJwtTokenException(TokenErrorCode.INVALID_ACCESS_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new InvalidJwtTokenException(TokenErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new InvalidJwtTokenException(TokenErrorCode.WRONG_TYPE_ACCESS_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new InvalidJwtTokenException(TokenErrorCode.UNKNOWN_ACCESS_TOKEN_ERROR);
        }
    }

    public void validateRefreshToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(refreshSecretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (SecurityException e) {
            log.info("Invalid JWT Signature", e);
            throw new InvalidJwtTokenException(TokenErrorCode.INVALID_SIGNATURE_IN_REFRESH_TOKEN);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new InvalidJwtTokenException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new InvalidJwtTokenException(TokenErrorCode.EXPIRED_REFRESH_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new InvalidJwtTokenException(TokenErrorCode.WRONG_TYPE_REFRESH_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new InvalidJwtTokenException(TokenErrorCode.UNKNOWN_REFRESH_TOKEN_ERROR);
        }
    }

    public String generateAccessToken(TokenPayload tokenPayload) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpires);
        return Jwts.builder()
                .claim("id", tokenPayload.getId())
                .claim("role", tokenPayload.getRole())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setHeaderParam("type", "jwt")
                .signWith(accessSecretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(TokenPayload tokenPayload) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshTokenExpires);
        return Jwts.builder()
                .claim("id", tokenPayload.getId())
                .claim("role", tokenPayload.getRole())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setHeaderParam("type", "jwt")
                .signWith(refreshSecretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Token extractBearerToken(HttpServletRequest request) {
        String accessToken = null;
        String accessTokenHeader = getAccessToken(request);
        if (accessTokenHeader != null) {
            accessToken = accessTokenHeader.substring(7);
        }

        String refreshToken = null;
        String refreshTokenHeader = getRefreshToken(request);
        if (refreshTokenHeader != null) {
            refreshToken = refreshTokenHeader.substring(7);
        }

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private static String getRefreshToken(HttpServletRequest request) {
        return request.getHeader(REFRESH_TOKEN);
    }

    private static String getAccessToken(HttpServletRequest request) {
        return request.getHeader(ACCESS_TOKEN);
    }

    public boolean validateRefreshTokenWithStoredToken(String refreshToken) {
        validateRefreshToken(refreshToken);

        Long id = getIdFromToken(refreshToken);
        Role role = getRoleFromToken(refreshToken);

        log.info("id = {}, role = {}", id, role);
        RefreshToken findRefreshToken = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 RefreshToken입니다."));


        return true;
    }

    public Role getRoleFromToken(String token) {
        Claims parsedClaims = parseClaims(token);
        return Role.of(parsedClaims.get("role").toString());
    }

    public Long getIdFromToken(String token) {
        Claims parsedClaims = parseClaims(token);
        return parsedClaims.get("id", Long.class);
    }

    public long getExpiredTime(String accessToken) {
        return parseClaims(accessToken)
                .getExpiration()
                .getTime();
    }
}
