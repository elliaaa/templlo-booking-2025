package com.templlo.service.user_coupon.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.templlo.service.coupon.entity.Coupon;
import com.templlo.service.coupon.repository.CouponRepository;
import com.templlo.service.user_coupon.entity.UserCoupon;
import com.templlo.service.user_coupon.repository.UserCouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCouponService {

	private final UserCouponRepository userCouponRepository;
	private final CouponRepository couponRepository;

	@Transactional
	public UserCoupon issueUserCoupon(UUID userId, UUID couponId, UUID promotionId, String loginId) {
		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Coupon ID입니다."));

		if (!coupon.getPromotion().getPromotionId().equals(promotionId)) {
			throw new IllegalArgumentException("프로모션 ID가 쿠폰과 일치하지 않습니다.");
		}

		if (userCouponRepository.existsByUserIdAndCoupon_Promotion_PromotionId(userId, promotionId)) {
			throw new IllegalStateException("이 프로모션의 쿠폰은 이미 발급되었습니다.");
		}

		UserCoupon userCoupon = UserCoupon.builder()
			.userId(userId)
			.userLoginId(loginId) // 로그인 ID 저장
			.coupon(coupon)
			.status("ISSUED")
			.issuedAt(LocalDateTime.now())
			.build();

		coupon.updateStatus("ISSUED");
		couponRepository.save(coupon);

		return userCouponRepository.save(userCoupon);
	}

	@Transactional(readOnly = true)
	public Page<UserCoupon> getUserCoupons(UUID userId, int page, int size) {
		// Pageable 객체 생성
		PageRequest pageable = PageRequest.of(page, size);

		// UserCoupon 조회
		return userCouponRepository.findByUserId(userId, pageable);
	}

	@Transactional
	public UserCoupon useCoupon(UUID userCouponId) {
		// 1. UserCoupon 조회
		UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 UserCoupon ID입니다."));

		// 2. 이미 사용된 쿠폰인지 확인
		if ("USED".equals(userCoupon.getStatus())) {
			throw new IllegalStateException("이미 사용된 쿠폰입니다.");
		}

		// 3. 상태 업데이트
		userCoupon.useCoupon();

		// 4. 쿠폰 상태 업데이트
		Coupon coupon = userCoupon.getCoupon();
		coupon.updateStatus("USED");
		couponRepository.save(coupon);

		// 5. 저장 및 반환
		return userCouponRepository.save(userCoupon);
	}
}
