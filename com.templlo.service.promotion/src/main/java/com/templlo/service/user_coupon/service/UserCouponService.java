package com.templlo.service.user_coupon.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.templlo.service.user_coupon.entity.UserCoupon;
import com.templlo.service.user_coupon.repository.UserCouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCouponService {

	private final UserCouponRepository userCouponRepository;

	@Transactional
	public UserCoupon issueUserCoupon(UUID userId, UUID couponId, UUID promotionId) {
		UserCoupon userCoupon = UserCoupon.builder()
			.userId(userId)
			.couponId(couponId)
			.promotionId(promotionId)
			.issuedAt(LocalDateTime.now())
			.isUsed(false)
			.build();

		return userCouponRepository.save(userCoupon);
	}

	@Transactional(readOnly = true)
	public List<UserCoupon> getUserCoupons(UUID userId) {
		return userCouponRepository.findByUserId(userId);
	}

	@Transactional
	public UserCoupon useCoupon(UUID userCouponId) {
		UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 UserCoupon ID입니다."));

		if (userCoupon.isUsed()) {
			throw new IllegalStateException("이미 사용된 쿠폰입니다.");
		}

		return userCouponRepository.save(userCoupon.markAsUsed());
	}
}
