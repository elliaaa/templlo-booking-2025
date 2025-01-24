package com.templlo.service.coupon.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.templlo.service.common.exception.BaseException;
import com.templlo.service.common.response.ApiResponse;
import com.templlo.service.common.response.BasicStatusCode;
import com.templlo.service.coupon.strategy.AmountDiscount;
import com.templlo.service.coupon.strategy.DiscountStrategy;
import com.templlo.service.coupon.strategy.PercentDiscount;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.templlo.service.common.aop.DistributedLock;
import com.templlo.service.common.aop.OutboxPublisher;
import com.templlo.service.common.client.ProgramFeignClient;
import com.templlo.service.common.dto.DetailProgramResponse;
import com.templlo.service.coupon.dto.CouponDeleteResponseDto;
import com.templlo.service.coupon.dto.CouponIssueResponseDto;
import com.templlo.service.coupon.dto.CouponStatusResponseDto;
import com.templlo.service.coupon.dto.CouponTransferResponseDto;
import com.templlo.service.coupon.dto.CouponUpdateRequestDto;
import com.templlo.service.coupon.dto.CouponUpdateResponseDto;
import com.templlo.service.coupon.dto.CouponUseResponseDto;
import com.templlo.service.coupon.dto.CouponValidationResponseDto;
import com.templlo.service.coupon.entity.Coupon;
import com.templlo.service.coupon.helper.RedisPromotionHelper;
import com.templlo.service.coupon.repository.CouponRepository;
import com.templlo.service.kafka.KafkaProducerService;
import com.templlo.service.outbox.repository.OutboxRepository;
import com.templlo.service.promotion.entity.Promotion;
import com.templlo.service.promotion.repository.PromotionRepository;
import com.templlo.service.user_coupon.entity.UserCoupon;
import com.templlo.service.user_coupon.repository.UserCouponRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {

	private final CouponRepository couponRepository;
	private final UserCouponRepository userCouponRepository;
	private final PromotionRepository promotionRepository;
	private final RedissonClient redissonClient;
	private final KafkaProducerService kafkaProducerService;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ProgramFeignClient programFeignClient;
	private final RedisPromotionHelper redisPromotionHelper;
	private final OutboxRepository outboxRepository;
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	@DistributedLock(key = "'promotion:lock:' + #promotionId", waitTime = 15, leaseTime = 10)
	@OutboxPublisher(
		eventType = "COUPON_ISSUED",
		payloadExpression = "{'couponId': #response.couponId, 'userId': #arg2, 'promotionId': #arg0}" // ObjectMapper 호출 제거
	)
	public CouponIssueResponseDto issueCoupon(UUID promotionId, String gender, UUID userId, String userLoginId) {
		log.debug("issueCoupon 호출: userLoginId={}", userLoginId);

		// 프로모션 조회
		Promotion promotion = promotionRepository.findById(promotionId)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 프로모션 ID입니다."));

		// Redis를 통한 쿠폰 카운터 초기화
		redisPromotionHelper.initializeRedisPromotionCounters(promotionId, promotion.getTotalCoupons());

		// Redis Lua 스크립트를 사용하여 남은 쿠폰 수 감소
		boolean success = decrementRemainingCouponsAtomically(promotionId);
		if (!success) {
			throw new IllegalStateException("남은 쿠폰 수량이 부족합니다.");
		}

		// 사용 가능한 쿠폰 조회 및 상태 업데이트
		Coupon coupon = fetchAndMarkCoupon(promotion, gender);

		// UserCoupon 생성 및 저장
		UserCoupon userCoupon = UserCoupon.builder()
			.userId(userId)
			.userLoginId(userLoginId)
			.coupon(coupon)
			.status("ISSUED")
			.issuedAt(LocalDateTime.now())
			.createdBy(userLoginId)
			.updatedBy(userLoginId)
			.build();
		userCouponRepository.save(userCoupon);

		log.info("쿠폰 발급 성공: userId={}, promotionId={}, couponId={}", userId, promotionId, coupon.getCouponId());
		return new CouponIssueResponseDto("SUCCESS", coupon.getCouponId(), "쿠폰이 발급되었습니다.");
	}

	private Coupon fetchAndMarkCoupon(Promotion promotion, String gender) {
		Coupon coupon;
		if (gender != null) {
			coupon = couponRepository.findFirstByPromotionAndGenderAndStatus(promotion, gender, "AVAILABLE")
				.orElseThrow(() -> new IllegalStateException("해당 성별에 사용 가능한 쿠폰이 없습니다."));
		} else {
			coupon = couponRepository.findFirstByPromotionAndStatus(promotion, "AVAILABLE")
				.orElseThrow(() -> new IllegalStateException("사용 가능한 쿠폰이 없습니다."));
		}

		// 쿠폰 상태를 ISSUED로 업데이트
		coupon.updateStatus("ISSUED");
		couponRepository.save(coupon); // 변경된 상태 저장
		log.debug("쿠폰 상태 업데이트: couponId={}, status={}", coupon.getCouponId(), coupon.getStatus());
		return coupon;
	}

	@Transactional
	public CouponUpdateResponseDto updateCoupon(UUID couponId, CouponUpdateRequestDto requestDto) {
		// 쿠폰 조회
		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 쿠폰 ID입니다."));

		// 상태 업데이트
		coupon.updateStatus(requestDto.status());
		couponRepository.save(coupon);

		// 응답 생성
		return new CouponUpdateResponseDto(
			"SUCCESS",
			coupon.getCouponId().toString(),
			"쿠폰 상태가 성공적으로 수정되었습니다."
		);
	}

	@Transactional
	public CouponUpdateResponseDto deleteCoupon(UUID couponId, String userId) {
		// 쿠폰 조회
		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 쿠폰 ID입니다."));

		// Soft Delete 처리
		coupon.delete(userId);
		coupon.updateStatus("DELETED");
		couponRepository.save(coupon);

		// 응답 생성
		return new CouponUpdateResponseDto(
			"SUCCESS",
			coupon.getCouponId().toString(),
			"쿠폰이 성공적으로 삭제(비활성화)되었습니다."
		);
	}

	@Cacheable(value = "couponStatus", key = "#promotionId", unless = "#result == null")
	@Transactional(readOnly = true)
	public CouponStatusResponseDto getCouponStatus(UUID promotionId) {
		Promotion promotion = promotionRepository.findById(promotionId)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 프로모션 ID입니다."));

		return new CouponStatusResponseDto(
			promotion.getPromotionId().toString(),
			promotion.getTotalCoupons(),
			promotion.getIssuedCoupons(),
			promotion.getRemainingCoupons()
		);
	}

	@Transactional(readOnly = true)
	public CouponValidationResponseDto validateCoupon(UUID couponId) {
		// 1. 쿠폰 조회
		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 쿠폰 ID입니다."));

		// 2. 쿠폰 상태 확인
		if (coupon.getStatus().equals("AVAILABLE")) {
			return new CouponValidationResponseDto(true, "쿠폰이 유효합니다.");
		} else if (coupon.getStatus().equals("ISSUED")) {
			return new CouponValidationResponseDto(false, "쿠폰이 이미 발급되었습니다.");
		} else if (coupon.getStatus().equals("EXPIRED")) {
			return new CouponValidationResponseDto(false, "쿠폰이 만료되었습니다.");
		}

		return new CouponValidationResponseDto(false, "알 수 없는 쿠폰 상태입니다.");
	}

	@Transactional
	public CouponUseResponseDto useCoupon(UUID couponId, UUID programId, LocalDate programDate) {
		// 1. 쿠폰 조회
		Coupon coupon = getCoupon(couponId);

		// 2. 프로그램 정보 조회 (Feign Client 사용)
		DetailProgramResponse program = getDetailProgramResponse(programId, programDate);

		// 3,4 : 쿠폰-프로그램 간 사용 가능 여부와 쿠폰 상태 검증
		coupon.checkUsable(program.type().name());

		// 5. 할인 금액 계산 정책 지정
		int discountValue = coupon.getValue().intValue();
		log.info("coupon.getDiscountType() = {}", coupon.getDiscountType());
		DiscountStrategy discountStrategy = switch (coupon.getDiscountType()) { // ??
			case "PERCENTAGE" -> new PercentDiscount(discountValue);
			case "AMOUNT" -> new AmountDiscount(discountValue);
			default -> throw new BaseException(BasicStatusCode.INTERNAL_SERVER_ERROR);
		};

		// 할인 금액 계산
		int finalPrice = discountStrategy.getDiscountPrice(program.programFee());

		// 6. 쿠폰 상태 업데이트
		coupon.updateStatus("USED");

		// 7. 응답 생성
		return new CouponUseResponseDto("SUCCESS", "쿠폰이 사용되었습니다.", finalPrice);
	}

	private DetailProgramResponse getDetailProgramResponse(UUID programId, LocalDate programDate) {
		ApiResponse<DetailProgramResponse> programResponse = programFeignClient.getProgram(programId);

		if (programResponse == null || programResponse.data() == null) {
			throw new IllegalStateException("프로그램 정보를 가져올 수 없습니다.");
		}

        return programResponse.data();
	}

	private Coupon getCoupon(UUID couponId) {
		return couponRepository.findById(couponId)
				.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 쿠폰 ID입니다."));
	}

	@Transactional
	public CouponTransferResponseDto transferCoupon(UUID couponId, UUID toUserId) {
		// 1. `UserCoupon`에서 쿠폰 조회
		UserCoupon userCoupon = userCouponRepository.findByCouponId(couponId)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 쿠폰 ID입니다."));

		// 2. 쿠폰 상태 확인
		if ("USED".equals(userCoupon.getStatus())) {
			throw new IllegalStateException("사용된 쿠폰은 양도할 수 없습니다.");
		}

		// 3. 쿠폰 양도 처리
		userCoupon = userCoupon.toBuilder()
			.userId(toUserId) // 새로운 사용자 ID로 변경
			.fromUserId(userCoupon.getUserId()) // 기존 사용자 ID 기록
			.transferredAt(LocalDateTime.now()) // 양도 날짜 기록
			.build();

		userCouponRepository.save(userCoupon);

		// 4. 응답 생성
		return new CouponTransferResponseDto(
			"SUCCESS",
			"쿠폰이 성공적으로 양도되었습니다."
		);
	}

	public Page<UserCoupon> getUserCoupons(UUID userId, UUID promotionId, String status, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		// `UserCouponRepository`를 통해 사용자 ID, 프로모션 ID 및 상태로 필터링
		if (promotionId != null && status != null) {
			return userCouponRepository.findByUserIdAndPromotionIdAndStatus(userId, promotionId, status, pageable);
		} else if (promotionId != null) {
			return userCouponRepository.findByUserIdAndPromotionId(userId, promotionId, pageable);
		} else if (status != null) {
			return userCouponRepository.findByUserIdAndStatus(userId, status, pageable);
		} else {
			return userCouponRepository.findByUserId(userId, pageable);
		}
	}

	@Transactional(readOnly = true)
	public Page<UserCoupon> getMyCoupons(String loginId, UUID promotionId, String status, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		// `UserCouponRepository`를 통해 로그인 ID를 기반으로 본인 쿠폰 조회
		if (promotionId != null && status != null) {
			return userCouponRepository.findByUserLoginIdAndPromotionIdAndStatus(loginId, promotionId, status,
				pageable);
		} else if (promotionId != null) {
			return userCouponRepository.findByUserLoginIdAndPromotionId(loginId, promotionId, pageable);
		} else if (status != null) {
			return userCouponRepository.findByUserLoginIdAndStatus(loginId, status, pageable);
		} else {
			return userCouponRepository.findByUserLoginId(loginId, pageable);
		}
	}

	@Transactional
	public CouponDeleteResponseDto deleteUserCoupon(UUID userId, UUID couponId) {
		// 1. `UserCoupon`에서 사용자 ID와 쿠폰 ID로 엔티티 조회
		UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(userId, couponId)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자가 가진 유효한 쿠폰이 없습니다."));

		// 2. 엔티티 삭제
		userCouponRepository.delete(userCoupon);

		// 3. 응답 생성
		return new CouponDeleteResponseDto(
			"SUCCESS",
			"쿠폰이 성공적으로 삭제되었습니다."
		);
	}

	@CacheEvict(value = "couponStatus", key = "#promotionId")
	@Transactional
	public void evictCouponStatusCache(UUID promotionId) {
		// 이 메서드는 캐시 무효화를 트리거하기 위해 사용됩니다.
	}

	private boolean decrementRemainingCouponsAtomically(UUID promotionId) {
		String luaScript =
			"local remaining = redis.call('GET', KEYS[1]) " +
				"if tonumber(remaining) > 0 then " +
				"    redis.call('DECR', KEYS[1]) " +
				"    return 1 " +
				"else " +
				"    return 0 " +
				"end";

		RedisScript<Long> redisScript = RedisScript.of(luaScript, Long.class);
		String key = "promotion:" + promotionId + ":remaining";

		// Redis Lua 스크립트를 실행하여 남은 쿠폰 수 감소
		Long result = redisPromotionHelper.executeScript(redisScript, List.of(key));

		log.debug("Redis Lua 스크립트 실행 결과: promotionId={}, remaining={}", promotionId, result);
		return result != null && result == 1;
	}

	@Transactional
	public void issueLevelUpCoupon(UUID userId) {
		// 1. 쿠폰 생성
		Coupon coupon = Coupon.builder()
			.type("LEVEL_UP")
			.status("ISSUED")
			.createdBy("SYSTEM") // 시스템 사용자로 설정
			.build();
		coupon = couponRepository.save(coupon);

		// 2. 유저 쿠폰 발급
		UserCoupon userCoupon = UserCoupon.builder()
			.userId(userId)
			.coupon(coupon)
			.status("ISSUED")
			.issuedAt(LocalDateTime.now())
			.createdBy("SYSTEM")
			.updatedBy("SYSTEM")
			.build();
		userCouponRepository.save(userCoupon);

		log.info("레벨 업 쿠폰 발급 완료: userId={}", userId);
	}

}