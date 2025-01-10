package com.templlo.service.temple.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "temple")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SearchTemple {

    @Id
    private String id;

    private String templeName;

    @Column(length = 500)
    private String templeDescription;

    private String templePhone;

    private SearchTempleAddress address;

    public static SearchTemple from(Temple temple) {
        return SearchTemple.builder()
                .id(temple.getTempleId().toString())
                .templeName(temple.getTempleName())
                .templeDescription(temple.getTempleDescription())
                .templePhone(temple.getTemplePhone())
                .address(SearchTempleAddress.builder()
                        .roadAddress(temple.getAddress().getRoadAddress())
                        .detailAddress(temple.getAddress().getDetailAddress())
                        .build())
                .build();
    }

}
