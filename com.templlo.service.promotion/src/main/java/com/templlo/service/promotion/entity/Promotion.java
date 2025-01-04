package com.templlo.service.promotion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "promotion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotion {

    @Id
    @GeneratedValue
    private UUID promotionId; // 프로모션 ID

    @Column(nullable = false)
    private String name; // 프로모션 이름

    @Column(nullable = false)
    private String type; // 프로모션 유형 (예: HOTDEAL, MEMBERSHIP 등)

    @Column(nullable = false)
    private LocalDate startDate; // 시작일

    @Column(nullable = false)
    private LocalDate endDate; // 종료일

    @Column(nullable = false)
    private String couponType; // 쿠폰 유형 (예: 할인, 입장권 등)

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer totalCoupons; // 전체 쿠폰 수량

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer issuedCoupons; // 발급된 쿠폰 수량

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer remainingCoupons; // 남은 쿠폰 수량

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일자

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정일자

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
