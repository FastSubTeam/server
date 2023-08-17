package com.popple.server.domain.user.service;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.user.dto.ValidateBusinessNumberRequestDto;
import com.popple.server.domain.user.repository.SellerRepository;
import com.popple.server.domain.user.vo.Fetch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SellerService {

    @Value("${open-api.url}")
    private String validateBusinessNumberApiUrl;

    @Value("${open-api.business-apikey}")
    private String validateBusinessNumberApikey;

    private final SellerRepository sellerRepository;

    public void checkDuplication(String nickname, String email) {

    }

    public APIDataResponse<?> checkExistBusinessNumberWithOpenAPI(ValidateBusinessNumberRequestDto validateBusinessNumberRequestDto) throws IOException {
        return Fetch.isValidBusinessNumber(validateBusinessNumberApiUrl, validateBusinessNumberApikey, validateBusinessNumberRequestDto);
    }
}
