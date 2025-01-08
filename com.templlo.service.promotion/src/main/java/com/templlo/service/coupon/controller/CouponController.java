package com.templlo.service.coupon.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.templlo.service.coupon.dto.CouponDeleteResponseDto;
import com.templlo.service.coupon.dto.CouponIssueResponseDto;
import com.templlo.service.coupon.dto.CouponStatusResponseDto;
import com.templlo.service.coupon.dto.CouponTransferResponseDto;
import com.templlo.service.coupon.dto.CouponUpdateRequestDto;
import com.templlo.service.coupon.dto.CouponUpdateResponseDto;
import com.templlo.service.coupon.dto.CouponUseRequestDto;
import com.templlo.service.coupon.dto.CouponUseResponseDto;
import com.templlo.service.coupon.dto.CouponValidationResponseDto;
import com.templlo.service.coupon.service.CouponService;
import com.templlo.service.user_coupon.entity.UserCoupon;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

	private final CouponService couponService;

	@PostMapping("/issue")
	public ResponseEntity<CouponIssueResponseDto> issueCoupon(
		@RequestParam String promotionId,
		@RequestParam(required = false) String gender) {

		UUID promotionUUID = UUID.fromString(promotionId);
		CouponIssueResponseDto response = couponService.issueCoupon(promotionUUID, gender);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{couponId}")
	public ResponseEntity<CouponUpdateResponseDto> updateCoupon(
		@PathVariable UUID couponId,
		@Valid @RequestBody CouponUpdateRequestDto requestDto
	) {
		CouponUpdateResponseDto response = couponService.updateCoupon(couponId, requestDto);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{couponId}")
	public ResponseEntity<CouponUpdateResponseDto> deleteCoupon(@PathVariable UUID couponId) {
		CouponUpdateResponseDto response = couponService.deleteCoupon(couponId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/promotion/{promotionId}/status")
	public ResponseEntity<CouponStatusResponseDto> getCouponStatus(@PathVariable UUID promotionId) {
		CouponStatusResponseDto response = couponService.getCouponStatus(promotionId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{couponId}/validate")
	public ResponseEntity<CouponValidationResponseDto> validateCoupon(@PathVariable UUID couponId) {
		CouponValidationResponseDto response = couponService.validateCoupon(couponId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{couponId}/use")
	public ResponseEntity<CouponUseResponseDto> useCoupon(
		@PathVariable UUID couponId,
		@RequestBody CouponUseRequestDto request
	) {
		CouponUseResponseDto response = couponService.useCoupon(couponId, request.programId());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{couponId}/transfer")
	public ResponseEntity<CouponTransferResponseDto> transferCoupon(
		@PathVariable UUID couponId,
		@RequestBody Map<String, String> request
	) {
		UUID toUserId = UUID.fromString(request.get("toUserId"));
		CouponTransferResponseDto response = couponService.transferCoupon(couponId, toUserId);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<Page<UserCoupon>> getUserCoupons(
		@RequestParam("user") UUID userId,
		@RequestParam(value = "promotion", required = false) UUID promotionId,
		@RequestParam(value = "status", required = false) String status,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size
	) {
		Page<UserCoupon> userCoupons = couponService.getUserCoupons(userId, promotionId, status, page, size);
		return ResponseEntity.ok(userCoupons);
	}

	@DeleteMapping("/user/{userId}/delete/{couponId}")
	public ResponseEntity<CouponDeleteResponseDto> deleteUserCoupon(
		@PathVariable UUID userId,
		@PathVariable UUID couponId
	) {
		CouponDeleteResponseDto response = couponService.deleteUserCoupon(userId, couponId);
		return ResponseEntity.ok(response);
	}

}
