package com.popple.server.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.popple.server.domain.entity.Member;
import com.popple.server.domain.user.dto.KakaoLoginRequestDto;
import com.popple.server.domain.user.dto.LoginResponseDto;
import com.popple.server.domain.user.vo.Fetch;
import com.popple.server.domain.user.vo.KakaoToken;
import com.popple.server.domain.user.vo.OAuthProfile;
import com.popple.server.domain.user.vo.TokenPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthService {

    @Value("${oauth2.kakao.client-id}")
    private String clientId;

    @Value("${oauth2.kakao.callback-url}")
    private String callbackUrl;

    private final MemberService memberService;
    private final TokenService tokenService;

    public void redirectToKakaoLoginPage(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("https://kauth.kakao.com/oauth/authorize?client_id="+clientId+"&redirect_uri="+callbackUrl+"&response_type=code&scope=account_email,profile_nickname");
    }

    @Transactional
    public LoginResponseDto loginWithKakaoAccessToken(String kakaoAccessToken) throws JsonProcessingException {
//        MultiValueMap<String, String> body = createBodyForKakaoToken(code);
//
//        ResponseEntity<String> kakaoTokenFromKakao = Fetch.getKakaoTokenWithKakaoAPI("https://kauth.kakao.com/oauth/token", HttpMethod.POST, body);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//        KakaoToken kakaoToken = objectMapper.readValue(kakaoTokenFromKakao.getBody(), KakaoToken.class);

        ResponseEntity<String> userInformationWithKakaoAccessToken = Fetch.getUserInformationWithKakaoAccessToken("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoAccessToken);

        OAuthProfile oAuthProfile = objectMapper.readValue(userInformationWithKakaoAccessToken.getBody(), OAuthProfile.class);

        Member findMember = memberService.getOptionalUserByEmail(oAuthProfile.getKakaoAcount().getEmail());

        if (findMember == null) {
            KakaoLoginRequestDto kakaoLoginRequestDto = KakaoLoginRequestDto.builder()
                    .email(oAuthProfile.getKakaoAcount().getEmail())
                    .nickname("kakao_" + oAuthProfile.getId())
                    .build();


            findMember = memberService.createKakaoMember(kakaoLoginRequestDto);
        }

        TokenPayload tokenPayload = findMember.toPayload();
        String accessToken = tokenService.generateAccessToken(tokenPayload);
        String refreshToken = tokenService.generateRefreshToken(tokenPayload);

        return LoginResponseDto.builder()
                .userId(findMember.getId())
                .role(tokenPayload.getRole())
                .email(findMember.getEmail())
                .profileImgUrl(findMember.getProfileImgUrl())
                .nickname(findMember.getNickname())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    @Transactional
    public LoginResponseDto loginWithKakaoCode(String code) throws JsonProcessingException {
        MultiValueMap<String, String> body = createBodyForKakaoToken(code);

        ResponseEntity<String> kakaoTokenFromKakao = Fetch.getKakaoTokenWithKakaoAPI("https://kauth.kakao.com/oauth/token", HttpMethod.POST, body);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        KakaoToken kakaoToken = objectMapper.readValue(kakaoTokenFromKakao.getBody(), KakaoToken.class);

        ResponseEntity<String> userInformationWithKakaoAccessToken = Fetch.getUserInformationWithKakaoAccessToken("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoToken.getAccessToken());

        OAuthProfile oAuthProfile = objectMapper.readValue(userInformationWithKakaoAccessToken.getBody(), OAuthProfile.class);

        Member findMember = memberService.getOptionalUserByEmail(oAuthProfile.getKakaoAcount().getEmail());

        if (findMember == null) {
            KakaoLoginRequestDto kakaoLoginRequestDto = KakaoLoginRequestDto.builder()
                    .email(oAuthProfile.getKakaoAcount().getEmail())
                    .nickname("kakao_" + oAuthProfile.getId())
                    .build();


            findMember = memberService.createKakaoMember(kakaoLoginRequestDto);
        }

        TokenPayload tokenPayload = findMember.toPayload();
        String accessToken = tokenService.generateAccessToken(tokenPayload);
        String refreshToken = tokenService.generateRefreshToken(tokenPayload);

        return LoginResponseDto.builder()
                .userId(findMember.getId())
                .email(findMember.getEmail())
                .profileImgUrl(findMember.getProfileImgUrl())
                .nickname(findMember.getNickname())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    private MultiValueMap<String, String> createBodyForKakaoToken(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", callbackUrl);
        body.add("code", code);
        return body;
    }
}
