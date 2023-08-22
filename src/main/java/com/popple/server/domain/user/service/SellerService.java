package com.popple.server.domain.user.service;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.entity.Seller;
import com.popple.server.domain.user.dto.CreateSellerRequestDto;
import com.popple.server.domain.user.dto.ValidateBusinessNumberRequestDto;
import com.popple.server.domain.user.exception.AlreadyExistException;
import com.popple.server.domain.user.exception.UserBadRequestException;
import com.popple.server.domain.user.exception.UserErrorCode;
import com.popple.server.domain.user.repository.CategoryRepository;
import com.popple.server.domain.user.repository.SellerCategoryRepository;
import com.popple.server.domain.user.repository.SellerRepository;
import com.popple.server.domain.user.vo.Fetch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService {

    @Value("${open-api.url}")
    private String validateBusinessNumberApiUrl;

    @Value("${open-api.business-apikey}")
    private String validateBusinessNumberApikey;

    @Value("${kakao.apikey}")
    private String kakaoApiKey;


    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final CategoryRepository categoryRepository;

    private final SellerCategoryRepository sellerCategoryRepository;
    private final SellerRepository sellerRepository;

    public void checkDuplication(String nickname, String email, String businessNumber) {
        validateDuplicatedEmail(email);
        validateDuplicatedNickname(nickname);
        validateDuplicatedBusinessNumber(businessNumber);
    }

    public void validateDuplicatedBusinessNumber(String businessNumber) {
        if (businessNumber != null) {
            if (sellerRepository.existsByBusinessNumber(businessNumber)) {
                throw new AlreadyExistException(UserErrorCode.EXIST_BUSINESS_NUMBER);
            }
        }
    }

    public void validateDuplicatedNickname(String nickname) {
        if (nickname != null) {
            if (sellerRepository.existsByNickname(nickname)) {
                throw new AlreadyExistException(UserErrorCode.EXIST_NICKNAME);
            }
        }
    }

    public void validateDuplicatedEmail(String email) {
        if (email != null) {
            if (sellerRepository.existsByEmail(email)) {
                throw new AlreadyExistException(UserErrorCode.EXIST_EMAIL);
            }
        }
    }

    public void checkBusinessNumberValidity(String businessNumber) throws IOException {
        ValidateBusinessNumberRequestDto validateBusinessNumberRequestDto = ValidateBusinessNumberRequestDto.builder()
                .b_no(new ArrayList<>(List.of(businessNumber)))
                .build();
        Fetch.checkBusinessNumberValidity(validateBusinessNumberApiUrl, validateBusinessNumberApikey, validateBusinessNumberRequestDto);
    }

    public APIDataResponse<?> checkExistBusinessNumberWithOpenAPI(ValidateBusinessNumberRequestDto validateBusinessNumberRequestDto) throws IOException {
        return Fetch.isValidBusinessNumber(validateBusinessNumberApiUrl, validateBusinessNumberApikey, validateBusinessNumberRequestDto);
    }

    public void create(CreateSellerRequestDto createSellerRequestDto) {
        Fetch.isValidAddress(kakaoApiKey, createSellerRequestDto.getAddress());

        Seller newSeller = Seller.builder()
                .email(createSellerRequestDto.getEmail())
                .nickname(createSellerRequestDto.getNickname())
                .password(bCryptPasswordEncoder.encode(createSellerRequestDto.getPassword()))
                .address(createSellerRequestDto.getAddress())
                .shopName(createSellerRequestDto.getShopName())
                .businessNumber(createSellerRequestDto.getBusinessNumber())
                .build();


//        List<SellerCategory> sellerCategories = categoryRepository.findByIdIn(createSellerRequestDto.getCategories()).stream()
//                .map(category -> {
//                    return SellerCategory.builder()
//                            .seller(newSeller)
//                            .category(category)
//                            .build();
//                })
//                .collect(Collectors.toList());

//        sellerCategoryRepository.saveAll(sellerCategories);
        sellerRepository.save(newSeller);
    }

    public Seller getUser(String email, String password) {

        Seller findSeller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new UserBadRequestException(UserErrorCode.NOT_FOUND));

        if (!bCryptPasswordEncoder.matches(password, findSeller.getPassword())) {
            throw new UserBadRequestException(UserErrorCode.INVALID_LOGIN_PAYLOAD);

        }

        return findSeller;

    }
}
