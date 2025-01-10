package com.templlo.service.temple.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateTempleRequest {
    private String templeName;
    private String templeDescription;
    private String templePhone;
    private String roadAddress;
    private String detailAddress;
}
