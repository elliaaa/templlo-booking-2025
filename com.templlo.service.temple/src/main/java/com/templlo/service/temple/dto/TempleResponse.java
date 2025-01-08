package com.templlo.service.temple.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.templlo.service.temple.model.SearchTemple;
import com.templlo.service.temple.model.Temple;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TempleResponse implements Serializable {
    private String templeName;
    private String templeDescription;
    private String templePhone;
    private String roadAddress;
    private String detailAddress;

    public static TempleResponse from(Temple temple) {
        return TempleResponse.builder()
                .templeName(temple.getTempleName())
                .templeDescription(temple.getTempleDescription())
                .templePhone(temple.getTemplePhone())
                .roadAddress(temple.getAddress().getRoadAddress())
                .detailAddress(temple.getAddress().getDetailAddress())
                .build();
    }

    public static TempleResponse from(SearchTemple temple) { //elasticSearch용
        return TempleResponse.builder()
                .templeName(temple.getTempleName())
                .templeDescription(temple.getTempleDescription())
                .templePhone(temple.getTemplePhone())
                .roadAddress(temple.getAddress().getRoadAddress())
                .detailAddress(temple.getAddress().getDetailAddress())
                .build();
    }
}
