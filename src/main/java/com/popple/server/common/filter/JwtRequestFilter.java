package com.popple.server.common.filter;

import com.popple.server.domain.user.exception.InvalidJwtTokenException;
import com.popple.server.domain.user.exception.TokenErrorCode;
import com.popple.server.domain.user.jwt.TokenManager;
import com.popple.server.domain.user.vo.Role;
import com.popple.server.domain.user.vo.Token;
import com.popple.server.domain.user.vo.TokenPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// TODO
//@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        List<String> permittedUrls = List.of("/api/auth/signup", "/api/auth/signin", "/api/auth/verify-email");

        if (request.getRequestURI().contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }


//        if (permittedUrls.contains(request.getRequestURI())) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        //TODO 추후 RTR로 수정하기 (Redis key값 = 유저 id, value값 = refreshtoken + accessToken 만료시마다 refreshtoken도 재발급 하기)

        Token token = tokenManager.extractBearerToken(request);

        if (isExistAccessToken(token)) {
            if (tokenManager.validateAccessToken(token.getAccessToken())) {
                setContextHolder(token.getAccessToken());
                filterChain.doFilter(request, response);
                return;
            }
        }

        if (isExistRefreshToken(token)) {
            if (isValidRefreshToken(token.getRefreshToken())) {
                Long id = tokenManager.getIdFromToken(token.getRefreshToken());
                Role role = tokenManager.getRoleFromToken(token.getRefreshToken());
                TokenPayload tokenPayload = TokenPayload.builder()
                        .id(id)
                        .role(role)
                        .build();
                String newAccessToken = tokenManager.generateAccessToken(tokenPayload);
                response.setHeader("accessToken", newAccessToken);
                log.info("newAccessToken => {}", newAccessToken);
                setContextHolder(newAccessToken);
                filterChain.doFilter(request, response);
                return;
            } else {
                throw new InvalidJwtTokenException(TokenErrorCode.INVALID_REFRESH_TOKEN);
            }
        }

        throw new InvalidJwtTokenException(TokenErrorCode.NOT_EXIST_TOKEN);

    }

    private boolean isValidRefreshToken(String refreshToken) {
        return tokenManager.validateRefreshTokenWithStoredToken(refreshToken);
    }

    private boolean isExistRefreshToken(Token token) {
        return token.getRefreshToken() != null;
    }

    private void setContextHolder(String accessToken) {
        Authentication authentication = tokenManager.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean isExistAccessToken(Token token) {
        return token.getAccessToken() != null;
    }
}
