package com.popple.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popple.server.common.dto.APIErrorResponse;
import com.popple.server.domain.user.exception.ErrorCode;
import com.popple.server.domain.user.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");

        accessDeniedException.printStackTrace();
        setResponse(response, Objects.requireNonNullElse(errorCode, UserErrorCode.FORBIDDEN));
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        APIErrorResponse errorResponse = APIErrorResponse.of(errorCode.getHttpStatus(), errorCode.getErrorMessage());
        String errorJson = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().print(errorJson);
    }
}
