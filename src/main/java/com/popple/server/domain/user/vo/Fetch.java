package com.popple.server.domain.user.vo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.user.dto.KakaoAddressApiResponseDto;
import com.popple.server.domain.user.dto.ValidateBusinessNumberRequestDto;
import com.popple.server.domain.user.dto.ValidateBusinessNumberResponseDto;
import com.popple.server.domain.user.exception.SellerErrorCode;
import com.popple.server.domain.user.exception.UserBadRequestException;
import com.popple.server.domain.user.exception.UserErrorCode;
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
        checkBusinessNumberValidity(baseApiUrl, apiKey, validateBusinessNumberRequestDto);


        return APIDataResponse.empty(HttpStatus.OK);
    }

    static void checkBusinessNumberValidity(String baseApiUrl, String apiKey, ValidateBusinessNumberRequestDto validateBusinessNumberRequestDto) throws java.io.IOException {
        ValidateBusinessNumberResponseDto result = fetchBusinessNumber(baseApiUrl, apiKey, validateBusinessNumberRequestDto);
        validateBusinessNumber(result);
    }

    private static ValidateBusinessNumberResponseDto fetchBusinessNumber(String baseApiUrl, String apiKey, ValidateBusinessNumberRequestDto validateBusinessNumberRequestDto) throws java.io.IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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
        return result;
    }

    static void validateBusinessNumber(ValidateBusinessNumberResponseDto result) {
        if (result.getMatchCount() == null) {
            throw new UserBadRequestException(UserErrorCode.INVALID_BUSINESS_NUMBER);
        }
    }

    static void isValidAddress(String kakaoApiKey, String address) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "KakaoAK " + kakaoApiKey);
        String url = "https://dapi.kakao.com/v2/local/search/address?query=" + address;

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
        KakaoAddressApiResponseDto response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, KakaoAddressApiResponseDto.class).getBody();

        if (response.getDocuments().size() == 0) {
            throw new UserBadRequestException(SellerErrorCode.INVALID_ADDRESS);
        }
        if (response.getDocuments().get(0).getAddress() == null) {
            throw new UserBadRequestException(SellerErrorCode.INVALID_ADDRESS);
        }

        System.out.println(response.getMeta().getTotalCount());
        if (response.getMeta().getTotalCount() == 0) {
            throw new UserBadRequestException(SellerErrorCode.INVALID_ADDRESS);
        }

        if (response.getDocuments().get(0).getRoadAddress() == null) {
            throw new UserBadRequestException(SellerErrorCode.INVALID_ADDRESS);
        }
    }
}