package com.popple.server.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class KakaoAddressApiResponseDto {

    @JsonProperty("documents")
    private List<KakaoAddressInformation> documents;

    @JsonProperty("meta")
    private KakaoAddressMetadata meta;

    @ToString
    @Getter
    public static class KakaoAddressInformation {

        @JsonProperty("address")
        private KakaoAddress address;

        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("address_type")
        private String addressType;

        @JsonProperty("x")
        private String x;

        @JsonProperty("y")
        private String y;

        @JsonProperty("road_address")
        private KakaoRoadAddress roadAddress;

    }

    @ToString
    @Getter
    public static class KakaoAddress {

        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("b_code")
        private String bCode;

        @JsonProperty("h_code")
        private String hCode;

        @JsonProperty("main_address_no")
        private String mainAddressNo;

        @JsonProperty("mountain_yn")
        private String mountainYn;

        @JsonProperty("region_1depth_name")
        private String region1DepthName;

        @JsonProperty("region_2depth_name")
        private String region2DepthName;

        @JsonProperty("region_3depth_name")
        private String region3DepthName;

        @JsonProperty("region_3depth_h_name")
        private String region3DepthHName;

        @JsonProperty("sub_address_no")
        private String subAddressNo;

        @JsonProperty("x")
        private String x;

        @JsonProperty("y")
        private String y;

    }

    @ToString
    @Getter
    public static class KakaoRoadAddress {

        @JsonProperty("address_name")
        private String addressName;
        @JsonProperty("building_name")
        private String buildingName;
        @JsonProperty("main_building_no")
        private String mainBuildingNo;
        @JsonProperty("region_1depth_name")
        private String region1DepthName;
        @JsonProperty("region_2depth_name")
        private String region2DepthName;
        @JsonProperty("region_3depth_name")
        private String region3DepthName;
        @JsonProperty("road_name")
        private String roadName;
        @JsonProperty("sub_building_no")
        private String subBuildingNo;
        @JsonProperty("underground_yn")
        private String undergroundYn;
        @JsonProperty("x")
        private String x;

        @JsonProperty("y")
        private String y;

        @JsonProperty("zone_no")
        private String zoneNo;
    }

    @ToString
    @Getter
    public static class KakaoAddressMetadata {

        @JsonProperty("is_end")
        private boolean isEnd;

        @JsonProperty("pageable_count")
        private int pageableCount;

        @JsonProperty("total_count")
        private int totalCount;
    }
}
