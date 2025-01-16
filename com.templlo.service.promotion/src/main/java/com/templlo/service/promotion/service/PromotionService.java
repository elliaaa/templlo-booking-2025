package com.templlo.service.promotion.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.templlo.service.common.aop.OutboxPublisher;
import com.templlo.service.common.aop.RoleCheck;
import com.templlo.service.common.response.PageResponse;
import com.templlo.service.coupon.entity.Coupon;
import com.templlo.service.coupon.repository.CouponRepository;
import com.templlo.service.kafka.KafkaProducerService;
import com.templlo.service.outbox.OutboxEvent;
import com.templlo.service.outbox.entity.OutboxMessage;
import com.templlo.service.outbox.repository.OutboxRepository;
import com.templlo.service.promotion.dto.PromotionCreatedEvent;
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
	private final KafkaProducerService kafkaProducerService;
	private final OutboxRepository outboxRepository;
	private final ApplicationEventPublisher eventPublisher;

	@RoleCheck(allowedRoles = {"MASTER", "ADMIN"})
	@OutboxPublisher(eventType = "PROMOTION_CREATED")
	@Transactional
	public PromotionResponseDto createPromotion(PromotionRequestDto requestDto, String userId, String role) {
		Promotion promotion = Promotion.builder()
			.name(requestDto.name())
			.type(requestDto.type())
			.startDate(requestDto.startDate())
			.endDate(requestDto.endDate())
			.couponType(requestDto.couponType())
			.totalCoupons(requestDto.totalCoupon())
			.remainingCoupons(requestDto.totalCoupon())
			.status("ACTIVE")
			.build();
		promotion.setCreatedBy(userId);
		promotionRepository.save(promotion);

		createCouponsForPromotion(promotion, requestDto);

		return new PromotionResponseDto(
			promotion.getPromotionId(),
			"SUCCESS",
			"프로모션이 생성되었습니다."
		);
	}

	private Promotion createAndSavePromotion(PromotionRequestDto requestDto, String userId) {
		Promotion promotion = Promotion.builder()
			.name(requestDto.name())
			.type(requestDto.type())
			.startDate(requestDto.startDate())
			.endDate(requestDto.endDate())
			.couponType(requestDto.couponType())
			.totalCoupons(requestDto.totalCoupon())
			.status("ACTIVE")
			.build();
		promotion.setCreatedBy(userId);
		return promotionRepository.save(promotion);
	}

	private OutboxMessage createAndSaveOutboxMessage(String eventType, String payload) {
		OutboxMessage outboxMessage = OutboxMessage.builder()
			.eventType(eventType)
			.payload(payload)
			.status("PENDING")
			.createdAt(LocalDateTime.now())
			.build();
		return outboxRepository.save(outboxMessage);
	}

	private void publishOutboxEvent(OutboxMessage outboxMessage) {
		eventPublisher.publishEvent(OutboxEvent.builder()
			.eventType(outboxMessage.getEventType())
			.payload(outboxMessage.getPayload())
			.timestamp(outboxMessage.getCreatedAt())
			.build());
	}

	@TransactionalEventListener
	public void sendPromotionCreatedEvent(Promotion promotion) {
		PromotionCreatedEvent event = new PromotionCreatedEvent(
			promotion.getPromotionId(),
			promotion.getName(),
			promotion.getStartDate(),
			promotion.getEndDate(),
			promotion.getCouponType(),
			promotion.getTotalCoupons()
		);

		kafkaProducerService.sendPromotionCreatedEvent(event);
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

		// Promotion 객체 복사 및 remainingCoupons 업데이트
		Promotion updatedPromotion = promotion.toBuilder()
			.remainingCoupons(remainingCoupons)
			.build();

		promotionRepository.save(updatedPromotion); // 변경된 객체 저장
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
