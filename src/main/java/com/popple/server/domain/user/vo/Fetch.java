package com.popple.server.domain.user.vo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.user.dto.ValidateBusinessNumberRequestDto;
import com.popple.server.domain.user.dto.ValidateBusinessNumberResponseDto;
import io.jsonwebtoken.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public interface Fetch {
    static ResponseEntity<String> getKakaoTokenWithKakaoAPI(String url, HttpMethod method, MultiValueMap<String, String> body) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> httpEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, httpEntity, String.class);
        return responseEntity;
    }

    static ResponseEntity<String> getUserInformationWithKakaoAccessToken(String url, HttpMethod method, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, httpEntity, String.class);
        return responseEntity;
    }

    static APIDataResponse<?> isValidBusinessNumber(
            String baseApiUrl, String apiKey, ValidateBusinessNumberRequestDto validateBusinessNumberRequestDto
    ) throws IOException, java.io.IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        System.out.println(validateBusinessNumberRequestDto);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), objectMapper.writeValueAsString(validateBusinessNumberRequestDto));
        Request request = new Request.Builder()
                .url(baseApiUrl + "?serviceKey=" + apiKey)
                .post(body)
                .build();

        String responseBody = okHttpClient.newCall(request)
                .execute()
                .body()
                .source()
                .readUtf8();

        ValidateBusinessNumberResponseDto result = objectMapper.readValue(responseBody, ValidateBusinessNumberResponseDto.class);

        if (result.getMatchCount() == null) {
            throw new RuntimeException("올바르지 않은 사업자 번호입니다");
        }


        return APIDataResponse.empty(HttpStatus.OK);
    }
}