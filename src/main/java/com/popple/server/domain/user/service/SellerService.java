package com.popple.server.domain.user.service;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.entity.Category;
import com.popple.server.domain.entity.Member;
import com.popple.server.domain.entity.Seller;
import com.popple.server.domain.entity.SellerCategory;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public void checkDuplication(String nickname, String email) {
        if (email != null) {
            if (sellerRepository.existsByEmail(email)) {
                throw new AlreadyExistException(UserErrorCode.EXIST_EMAIL);
            }
        }

        if (nickname != null) {
            if (sellerRepository.existsByNickname(nickname)) {
                throw new AlreadyExistException(UserErrorCode.EXIST_NICKNAME);
            }
        }
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
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        List<SellerCategory> sellerCategories = categoryRepository.findByIdIn(createSellerRequestDto.getCategories()).stream()
                .map(category -> {
                    return SellerCategory.builder()
                            .seller(newSeller)
                            .category(category)
                            .build();
                })
                .collect(Collectors.toList());

        sellerCategoryRepository.saveAll(sellerCategories);
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
