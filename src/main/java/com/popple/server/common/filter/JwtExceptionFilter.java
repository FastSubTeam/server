package com.popple.server.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popple.server.common.dto.APIErrorResponse;
import com.popple.server.domain.user.exception.InvalidJwtTokenException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;


@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

        try {
            filterChain.doFilter(request, response);
        } catch (InvalidJwtTokenException e) {
            APIErrorResponse errorResponse = APIErrorResponse.of(e.getErrorCode().getHttpStatus(), e.getMessage());
            String apiErrorResponse = objectMapper.writeValueAsString(errorResponse);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(apiErrorResponse);
        }

    }
}
