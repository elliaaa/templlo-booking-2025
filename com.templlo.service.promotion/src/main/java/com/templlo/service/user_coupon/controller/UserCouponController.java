package com.templlo.service.user_coupon.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	public ResponseEntity<UserCoupon> issueUserCoupon(@RequestParam UUID userId,
		@RequestParam UUID couponId,
		@RequestParam UUID promotionId) {
		return ResponseEntity.ok(userCouponService.issueUserCoupon(userId, couponId, promotionId));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<List<UserCoupon>> getUserCoupons(@PathVariable UUID userId) {
		return ResponseEntity.ok(userCouponService.getUserCoupons(userId));
	}

	@PostMapping("/{userCouponId}/use")
	public ResponseEntity<UserCoupon> useCoupon(@PathVariable UUID userCouponId) {
		return ResponseEntity.ok(userCouponService.useCoupon(userCouponId));
	}
}
