package com.templlo.service.user_coupon.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.templlo.service.user_coupon.entity.UserCoupon;
import com.templlo.service.user_coupon.service.UserCouponService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user-coupons")
@RequiredArgsConstructor
public class UserCouponController {

	private final UserCouponService userCouponService;

	@PostMapping("/issue")
	public ResponseEntity<UserCoupon> issueUserCoupon(
		@RequestParam UUID userId,
		@RequestParam UUID couponId,
		@RequestParam UUID promotionId,
		@RequestHeader("X-Login-Id") String loginId // 로그인 ID 추가
	) {
		UserCoupon issuedUserCoupon = userCouponService.issueUserCoupon(userId, couponId, promotionId, loginId);
		return ResponseEntity.ok(issuedUserCoupon);
	}

	// 특정 사용자(User)의 쿠폰 조회 (페이지네이션 추가)
	@GetMapping("/{userId}")
	public ResponseEntity<Page<UserCoupon>> getUserCoupons(
		@PathVariable UUID userId,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size) {
		Page<UserCoupon> userCoupons = userCouponService.getUserCoupons(userId, page, size);
		return ResponseEntity.ok(userCoupons);
	}

	// UserCoupon 사용
	@PostMapping("/{userCouponId}/use")
	public ResponseEntity<UserCoupon> useCoupon(@PathVariable UUID userCouponId) {
		UserCoupon usedCoupon = userCouponService.useCoupon(userCouponId);
		return ResponseEntity.ok(usedCoupon);
	}
}
