package com.popple.server.domain.user.jwt;

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
    private final SecretKey secretKey;
    private final int accessTokenExpires;
    private final int refreshTokenExpires;
    private final UserDetailsService userDetailsService;

    public TokenManager(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expires}") String accessTokenExpires,
            @Value("${jwt.refresh-token-expires}") String refreshTokenExpires,
            UserDetailsService userDetailsService

    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpires = Integer.parseInt(accessTokenExpires);
        this.refreshTokenExpires = Integer.parseInt(refreshTokenExpires);
        this.userDetailsService = userDetailsService;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        GrantedAuthority authority = new SimpleGrantedAuthority(claims.get("role").toString());

        UserDetails principal = userDetailsService.loadUserByUsername(claims.get("email").toString());

        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());

    }

    public String generateAccessToken(TokenPayload tokenPayload) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpires);
        return Jwts.builder()
                .signWith(secretKey)
                .claim("id", tokenPayload.getId())
                .claim("email", tokenPayload.getEmail())
                .claim("role", tokenPayload.getRole())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setHeaderParam("type", "jwt")
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException();
        }
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }

        return false;
    }

    public String generateRefreshToken(TokenPayload tokenPayload) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshTokenExpires);
        return Jwts.builder()
                .signWith(secretKey)
                .claim("id", tokenPayload.getId())
                .claim("role", tokenPayload.getRole())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setHeaderParam("type", "jwt")
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractBearerToken(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }
}
