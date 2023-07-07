package com.popple.server.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class AddressStoreTest {

    @Test
    public void validAddressTest() {
        String city = "서울특별시";
        String district = "강동구";

        Throwable throwable = catchThrowable(() -> AddressStore.validate(city, district));

        assertThat(throwable).isNull();
    }

    @ParameterizedTest
    @CsvSource(
            {
                    "'서울특별시', '강냄구'",
                    "'강남특별시', '경기도'",
                    "'강원특별자치도', '1'",
            }
    )
    public void invalidAddressTest(String city, String district) {
        Throwable throwable = catchThrowable(() -> AddressStore.validate(city, district));

        assertThat(throwable)
                .isInstanceOf(RuntimeException.class);
    }

}