package com.templlo.service.promotion.service;

import com.templlo.service.promotion.dto.PromotionRequestDto;
import com.templlo.service.promotion.dto.PromotionResponseDto;
import com.templlo.service.promotion.dto.PromotionUpdateDto;
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
                .maleCoupons(requestDto.getMaleCoupon())
                .femaleCoupons(requestDto.getFemaleCoupon())
                .totalCoupons(requestDto.getTotalCoupon())
                .issuedCoupons(0)
                .remainingCoupons(requestDto.getTotalCoupon())
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


    public PromotionResponseDto updatePromotion(UUID promotionId, PromotionUpdateDto updateDto) {
        // 1. 기존 프로모션 조회
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("프로모션을 찾을 수 없습니다."));

        // 2. 요청된 데이터로 프로모션 필드 업데이트
        promotion.updatePromotion(
                updateDto.getName(),
                updateDto.getStartDate(),
                updateDto.getEndDate(),
                updateDto.getMaleCoupons(),
                updateDto.getFemaleCoupons(),
                updateDto.getTotalCoupons(),
                updateDto.getCouponType() // couponType 업데이트 추가
        );

        // 3. 데이터베이스 저장
        promotionRepository.save(promotion);

        // 4. 성공 메시지 반환
        return PromotionResponseDto.builder()
                .promotionId(promotion.getPromotionId())
                .status("SUCCESS")
                .message("프로모션이 수정되었습니다.")
                .build();
    }

    public PromotionResponseDto deletePromotion(UUID promotionId) {
        // 1. 프로모션 조회
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("프로모션을 찾을 수 없습니다."));

        // 2. 프로모션 삭제
        promotionRepository.delete(promotion);

        // 3. 성공 메시지 반환
        return PromotionResponseDto.builder()
                .promotionId(promotionId)
                .status("SUCCESS")
                .message("프로모션이 삭제되었습니다.")
                .build();
    }

}
