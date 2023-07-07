package com.popple.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable(); // CSRF 해제
        httpSecurity.headers().frameOptions().disable(); // iframe 거부
        httpSecurity.cors().configurationSource(getConfigurationSource()); // cors 재설정
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // JSessionId가 응답되면 세션 영역에서 사라짐 (Stateless)
        httpSecurity.formLogin().disable(); // form 로그인 해제 (UsernamePasswordAuthenticationFilter 비활성화)
        httpSecurity.httpBasic().disable(); // 로그인 인증창이 뜨지 않도록 비활성화

        return httpSecurity.build();

    }

    private CorsConfigurationSource getConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // 모든 요청 허용, JS 요청 허용
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청하는 것을 허용
        configuration.addExposedHeader("Authorization"); // Authorization 헤더를 노출

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);

        return urlBasedCorsConfigurationSource;
    }

}
