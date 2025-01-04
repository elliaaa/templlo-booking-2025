package com.templlo.service.promotion.service;

import com.templlo.service.promotion.dto.PromotionRequestDto;
import com.templlo.service.promotion.dto.PromotionResponseDto;
import com.templlo.service.promotion.entity.Promotion;
import com.templlo.service.promotion.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionResponseDto createPromotion(PromotionRequestDto requestDto) {
        // 1. Promotion 엔티티 생성
        Promotion promotion = Promotion.builder()
                .name(requestDto.getName())
                .type(requestDto.getType())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .couponType(requestDto.getCouponType())
                .totalCoupons(requestDto.getTotalCoupons())
                .issuedCoupons(0)
                .remainingCoupons(requestDto.getTotalCoupons())
                .build();

        // 2. 데이터베이스 저장
        Promotion savedPromotion = promotionRepository.save(promotion);

        // 3. ResponseDto 생성 및 반환
        return PromotionResponseDto.builder()
                .promotionId(savedPromotion.getPromotionId())
                .status("SUCCESS")
                .message("프로모션이 생성되었습니다.")
                .build();
    }
}
