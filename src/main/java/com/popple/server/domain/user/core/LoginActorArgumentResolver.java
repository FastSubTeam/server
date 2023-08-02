package com.popple.server.domain.user.core;

import com.popple.server.domain.user.annotation.LoginActor;
import com.popple.server.domain.user.jwt.TokenManager;
import com.popple.server.domain.user.service.TokenService;
import com.popple.server.domain.user.vo.Actor;
import com.popple.server.domain.user.vo.Role;
import com.popple.server.domain.user.vo.Token;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Long id = Long.parseLong(authentication.getName());
        List<? extends GrantedAuthority> authorities = (List) authentication.getAuthorities();
        Role role = Role.of(authorities.get(0).toString());
        return Actor.builder()
                .id(id)
                .role(role)
                .build();
    }
}
