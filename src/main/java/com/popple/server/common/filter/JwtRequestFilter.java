package com.popple.server.common.filter;

import com.popple.server.domain.user.jwt.TokenManager;
import com.popple.server.domain.user.vo.Token;
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

// TODO
//@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //TODO 추후 RTR로 수정하기 (Redis key값 = 유저 id, value값 = refreshtoken + accessToken 만료시마다 refreshtoken도 재발급 하기)

        Token token = tokenManager.extractBearerToken(request);


        if (isExistAccessToken(token)) {
            if (tokenManager.validateAccessToken(token.getAccessToken())) {
                setContextHolder(token.getAccessToken());
                filterChain.doFilter(request, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setContextHolder(String accessToken) {
        Authentication authentication = tokenManager.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean isExistAccessToken(Token token) {
        return token.getAccessToken() != null;
    }
}
