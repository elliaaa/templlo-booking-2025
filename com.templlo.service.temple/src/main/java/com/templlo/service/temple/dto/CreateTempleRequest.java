package com.templlo.service.temple.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTempleRequest {

    @NotBlank(message = "사찰 이름은 필수 입력값입니다.")
    private String templeName;

    @NotBlank(message = "주소는 필수 입력값입니다.")
    private String roadAddress;

    @NotBlank(message = "상세 주소는 필수 입력값입니다.")
    private String detailAddress;

    private String templeDescription;
    private String templePhone;
}
