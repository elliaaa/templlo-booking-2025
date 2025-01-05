package com.templlo.service.promotion.service;

import com.templlo.service.promotion.dto.PromotionDetailResponseDto;
import com.templlo.service.promotion.dto.PromotionRequestDto;
import com.templlo.service.promotion.dto.PromotionResponseDto;
import com.templlo.service.promotion.dto.PromotionUpdateDto;
import com.templlo.service.promotion.entity.Promotion;
import com.templlo.service.promotion.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionResponseDto createPromotion(PromotionRequestDto requestDto) {
        // 1. Promotion 엔티티 생성
        Promotion promotion = Promotion.builder()
                .name(requestDto.name())
                .type(requestDto.type())
                .startDate(requestDto.startDate())
                .endDate(requestDto.endDate())
                .couponType(requestDto.couponType())
                .maleCoupons(requestDto.maleCoupon())
                .femaleCoupons(requestDto.femaleCoupon())
                .totalCoupons(requestDto.totalCoupon())
                .issuedCoupons(0)
                .remainingCoupons(requestDto.totalCoupon())
                .status("ACTIVE")
                .build();

        // 2. 데이터베이스 저장
        Promotion savedPromotion = promotionRepository.save(promotion);

        // 3. ResponseDto 생성 및 반환
        return new PromotionResponseDto(
                savedPromotion.getPromotionId(),
                "SUCCESS",
                "프로모션이 생성되었습니다."
        );
    }

    public PromotionResponseDto updatePromotion(UUID promotionId, PromotionUpdateDto updateDto) {
        // 1. 기존 프로모션 조회
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("프로모션을 찾을 수 없습니다."));

        // 2. 요청된 데이터로 프로모션 필드 업데이트
        promotion.updatePromotion(
                updateDto.name(),
                updateDto.startDate(),
                updateDto.endDate(),
                updateDto.maleCoupons(),
                updateDto.femaleCoupons(),
                updateDto.totalCoupons(),
                updateDto.couponType(),
                updateDto.status()
        );

        // 3. 데이터베이스 저장
        promotionRepository.save(promotion);

        // 4. 성공 메시지 반환
        return new PromotionResponseDto(
                promotion.getPromotionId(),
                "SUCCESS",
                "프로모션이 수정되었습니다."
        );
    }

    public PromotionResponseDto deletePromotion(UUID promotionId) {
        // 1. 프로모션 조회
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("프로모션을 찾을 수 없습니다."));

        // 2. 프로모션 삭제
        promotionRepository.delete(promotion);

        // 3. 성공 메시지 반환
        return new PromotionResponseDto(
                promotionId,
                "SUCCESS",
                "프로모션이 삭제되었습니다."
        );
    }

    public PromotionDetailResponseDto getPromotion(UUID promotionId) {
        // 1. 프로모션 조회
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("프로모션을 찾을 수 없습니다."));

        // 2. 조회 결과를 DTO로 변환
        return new PromotionDetailResponseDto(
                promotion.getPromotionId(),
                promotion.getName(),
                promotion.getType(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.getTotalCoupons(),
                promotion.getIssuedCoupons(),
                promotion.getRemainingCoupons(),
                promotion.getMaleCoupons(),
                promotion.getFemaleCoupons(),
                promotion.getStatus()
        );
    }

    public Page<PromotionDetailResponseDto> getPromotions(int page, int size, String type, String status) {
        // 1. 페이지네이션 요청 생성
        PageRequest pageRequest = PageRequest.of(page, size);

        // 2. 조건에 따라 데이터 조회
        Page<Promotion> promotions = promotionRepository.findByFilters(type, status, pageRequest);

        // 3. Promotion -> PromotionDetailResponseDto 매핑
        return promotions.map(promotion -> new PromotionDetailResponseDto(
                promotion.getPromotionId(),
                promotion.getName(),
                promotion.getType(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.getTotalCoupons(),
                promotion.getIssuedCoupons(),
                promotion.getRemainingCoupons(),
                promotion.getMaleCoupons(),
                promotion.getFemaleCoupons(),
                promotion.getStatus()
        ));
    }
}
