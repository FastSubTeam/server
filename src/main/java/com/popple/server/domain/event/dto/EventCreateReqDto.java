package com.popple.server.domain.event.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.popple.server.domain.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventCreateReqDto {
    private String name;
    private String description;
    private String location;
    private String thumbnailUrl;
    private Timestamp startDate;
    private Timestamp endDate;

}
