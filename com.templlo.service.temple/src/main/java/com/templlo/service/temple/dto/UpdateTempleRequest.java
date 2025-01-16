package com.templlo.service.temple.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTempleRequest {
    private String templeName;
    private String templeDescription;
    private String templePhone;
    private String roadAddress;
    private String detailAddress;
}
