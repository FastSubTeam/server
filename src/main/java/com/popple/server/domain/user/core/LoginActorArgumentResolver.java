package com.popple.server.domain.user.core;

import com.popple.server.domain.user.annotation.LoginActor;
import com.popple.server.domain.user.jwt.TokenManager;
import com.popple.server.domain.user.service.TokenService;
import com.popple.server.domain.user.vo.Actor;
import com.popple.server.domain.user.vo.Role;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Slf4j
public class LoginActorArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenManager tokenManager;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginActor.class) && parameter.getParameterType().equals(Actor.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = tokenManager.extractBearerToken(((ServletRequestAttributes) webRequest).getRequest());
        if (token == null || !tokenManager.validateToken(token)) {
            throw new RuntimeException();
        }

        Claims parseClaims = tokenManager.parseClaims(token);
        Long id = parseClaims.get("id", Long.class);
        String email = parseClaims.get("email", String.class);
        Role role = Role.of(parseClaims.get("role").toString());


        return Actor.builder()
                .id(id)
                .email(email)
                .role(role)
                .build();
    }
}
