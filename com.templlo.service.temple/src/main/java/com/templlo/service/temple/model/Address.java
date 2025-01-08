package com.templlo.service.temple.model;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class Address {
    private String roadAddress; // 도로명 주소

    private String detailAddress; // 상세 주소

    @Builder
    public Address(String roadAddress,String detailAddress) {
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }


}
