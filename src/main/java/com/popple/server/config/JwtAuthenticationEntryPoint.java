package com.popple.server.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.popple.server.common.dto.APIErrorResponse;
import com.popple.server.domain.user.exception.ErrorCode;
import com.popple.server.domain.user.exception.TokenErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");

        authException.printStackTrace();

        setResponse(response, Objects.requireNonNullElse(errorCode, TokenErrorCode.UNKNOWN_ACCESS_TOKEN_ERROR));
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        APIErrorResponse errorResponse = APIErrorResponse.of(errorCode.getHttpStatus(), errorCode.getErrorMessage());
        String errorJson = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().print(errorJson);
    }
}
