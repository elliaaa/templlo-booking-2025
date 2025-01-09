package com.templlo.service.temple.model;

import com.templlo.service.temple.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "temple")
public class Temple extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID templeId;

    private UUID userId; // 사용자 엔티티의 user_id (TEMPLE_ADMIN 역할)

    private String templeName;

    @Column(length = 500)
    private String templeDescription;

    private String templePhone;

    @Embedded
    private Address address;

    @Builder
    public static Temple of(String templeName, String templeDescription, String templePhone, String roadAddress, String detailAddress) {
        Temple temple = new Temple();
        temple.templeName = templeName;
        temple.templeDescription = templeDescription;
        temple.templePhone = templePhone;
        temple.address = Address.builder()
                .roadAddress(roadAddress)
                .detailAddress(detailAddress)
                .build();
        return temple;
    }

    public void setUserId(UUID userId) {this.userId = userId;}

    public void setTempleName(String templeName) {
        this.templeName = templeName;
    }

    public void setTempleDescription(String templeDescription) {
        this.templeDescription = templeDescription;
    }

    public void setTemplePhone(String templePhone) {
        this.templePhone = templePhone;
    }


}
