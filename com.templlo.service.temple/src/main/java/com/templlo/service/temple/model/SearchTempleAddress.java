package com.templlo.service.temple.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchTempleAddress {

    private String roadAddress; // 도로명 주소

    private String detailAddress; // 상세 주소

    public static SearchTempleAddress from(Address address) {
        return SearchTempleAddress.builder()
                .roadAddress(address.getRoadAddress())
                .detailAddress(address.getDetailAddress())
                .build();
    }
}
