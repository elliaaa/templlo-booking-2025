package com.templlo.service.promotion.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.templlo.service.common.response.PageResponse;
import com.templlo.service.coupon.entity.Coupon;
import com.templlo.service.coupon.repository.CouponRepository;
import com.templlo.service.promotion.dto.PromotionDetailResponseDto;
import com.templlo.service.promotion.dto.PromotionRequestDto;
import com.templlo.service.promotion.dto.PromotionResponseDto;
import com.templlo.service.promotion.dto.PromotionUpdateDto;
import com.templlo.service.promotion.entity.Promotion;
import com.templlo.service.promotion.repository.PromotionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionService {

	private final PromotionRepository promotionRepository;
	private final CouponRepository couponRepository;

	@Transactional
	public PromotionResponseDto createPromotion(PromotionRequestDto requestDto, String userId, String role) {
		// 권한 검증
		if (!"MASTER".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
			throw new IllegalArgumentException("권한이 부족합니다. 프로모션을 생성하려면 MASTER 또는 ADMIN 역할이 필요합니다.");
		}

		// 프로모션 엔티티 생성
		Promotion promotion = Promotion.builder()
			.name(requestDto.name())
			.type(requestDto.type())
			.startDate(requestDto.startDate())
			.endDate(requestDto.endDate())
			.couponType(requestDto.couponType())
			.maleCoupons(requestDto.maleCoupon() != null ? requestDto.maleCoupon() : 0)
			.femaleCoupons(requestDto.femaleCoupon() != null ? requestDto.femaleCoupon() : 0)
			.totalCoupons(requestDto.totalCoupon())
			.issuedCoupons(0)
			.remainingCoupons(requestDto.totalCoupon())
			.status(requestDto.status() != null ? requestDto.status() : "ACTIVE")
			.build();

		promotion.setCreatedBy(userId);
		promotion.setUpdatedBy(userId);

		promotion = promotionRepository.save(promotion);

		// 쿠폰 생성
		createCouponsForPromotion(promotion, requestDto);

		return new PromotionResponseDto(
			promotion.getPromotionId(),
			"SUCCESS",
			"프로모션이 생성되었습니다."
		);
	}

	private void createCouponsForPromotion(Promotion promotion, PromotionRequestDto requestDto) {
		int maleCouponCount = requestDto.maleCoupon() != null ? requestDto.maleCoupon() : 0;
		int femaleCouponCount = requestDto.femaleCoupon() != null ? requestDto.femaleCoupon() : 0;
		int remainingCoupons = requestDto.totalCoupon() - maleCouponCount - femaleCouponCount;

		BigDecimal value = requestDto.value(); // 할인 금액 또는 비율
		String discountType = requestDto.discountType();

		// 남성용 쿠폰 생성
		if (maleCouponCount > 0) {
			for (int i = 0; i < maleCouponCount; i++) {
				Coupon maleCoupon = Coupon.builder()
					.promotion(promotion)
					.type(requestDto.couponType())
					.discountType(discountType)
					.value(value)
					.gender("MALE")
					.status("AVAILABLE")
					.build();
				couponRepository.save(maleCoupon);
			}
		}

		// 여성용 쿠폰 생성
		if (femaleCouponCount > 0) {
			for (int i = 0; i < femaleCouponCount; i++) {
				Coupon femaleCoupon = Coupon.builder()
					.promotion(promotion)
					.type(requestDto.couponType())
					.discountType(discountType)
					.value(value)
					.gender("FEMALE")
					.status("AVAILABLE")
					.build();
				couponRepository.save(femaleCoupon);
			}
		}

		// 성별 구분 없는 쿠폰 생성
		if (remainingCoupons > 0) {
			for (int i = 0; i < remainingCoupons; i++) {
				Coupon generalCoupon = Coupon.builder()
					.promotion(promotion)
					.type(requestDto.couponType())
					.discountType(discountType)
					.value(value)
					.gender(null)
					.status("AVAILABLE")
					.build();
				couponRepository.save(generalCoupon);
			}
		}
	}

	@Transactional
	public PromotionResponseDto updatePromotion(UUID promotionId, PromotionUpdateDto updateDto, String userId) {
		// 기존 프로모션 조회
		Promotion promotion = promotionRepository.findById(promotionId)
			.orElseThrow(() -> new IllegalArgumentException("프로모션을 찾을 수 없습니다."));

		// 프로모션 업데이트
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

		// 저장
		promotionRepository.save(promotion);

		// 응답 DTO 생성
		return new PromotionResponseDto(
			promotion.getPromotionId(),
			"SUCCESS",
			"프로모션이 성공적으로 수정되었습니다."
		);
	}

	@Transactional
	public PromotionResponseDto deletePromotion(UUID promotionId, String userId) {
		// 기존 프로모션 조회
		Promotion promotion = promotionRepository.findById(promotionId)
			.orElseThrow(() -> new IllegalArgumentException("프로모션을 찾을 수 없습니다."));

		// 프로모션 삭제
		promotionRepository.delete(promotion);

		// 성공 메시지 반환
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

	@Transactional(readOnly = true)
	public PageResponse<PromotionDetailResponseDto> getPromotions(int page, int size, String type, String status) {
		// 1. 페이지네이션 요청 생성
		PageRequest pageRequest = PageRequest.of(page, size);

		// 2. 조건에 따라 데이터 조회
		Page<Promotion> promotions = promotionRepository.findByFilters(type, status, pageRequest);

		// 3. Promotion -> PromotionDetailResponseDto 매핑 및 PageResponse 생성
		return PageResponse.of(promotions.map(promotion -> new PromotionDetailResponseDto(
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
		)));
	}

}
