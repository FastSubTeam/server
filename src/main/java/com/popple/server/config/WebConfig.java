package com.popple.server.config;

import com.popple.server.common.converter.RoleRequestConverter;
import com.popple.server.domain.user.core.LoginActorArgumentResolver;
import com.popple.server.domain.user.jwt.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TokenManager tokenManager;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new RoleRequestConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginActorArgumentResolver(tokenManager));
    }
}
