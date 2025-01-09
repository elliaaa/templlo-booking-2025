package com.templlo.service.coupon.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.templlo.service.common.client.UserFeignClient;
import com.templlo.service.common.dto.UserResponse;
import com.templlo.service.common.response.ApiResponse;
import com.templlo.service.common.response.BasicStatusCode;
import com.templlo.service.coupon.dto.CouponDeleteResponseDto;
import com.templlo.service.coupon.dto.CouponIssueResponseDto;
import com.templlo.service.coupon.dto.CouponStatusResponseDto;
import com.templlo.service.coupon.dto.CouponTransferResponseDto;
import com.templlo.service.coupon.dto.CouponUpdateResponseDto;
import com.templlo.service.coupon.dto.CouponUseRequestDto;
import com.templlo.service.coupon.dto.CouponUseResponseDto;
import com.templlo.service.coupon.dto.CouponValidationResponseDto;
import com.templlo.service.coupon.service.CouponService;
import com.templlo.service.user_coupon.entity.UserCoupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

	private final CouponService couponService;
	private final UserFeignClient userFeignClient; // FeignClient 추가

	@PreAuthorize("hasAnyAuthority('MEMBER', 'TEMPLE_ADMIN', 'MASTER')")
	@PostMapping("/issue")
	public ResponseEntity<ApiResponse<CouponIssueResponseDto>> issueCoupon(
		@RequestParam String promotionId,
		@RequestParam(required = false) String gender,
		@RequestHeader(value = "X-Login-Id", required = true) String loginId,
		@RequestHeader(value = "X-User-Role", required = true) String role
	) {
		try {
			if (promotionId == null || promotionId.isBlank()) {
				throw new IllegalArgumentException("Promotion ID는 null이거나 비어 있을 수 없습니다.");
			}

			UUID promotionUUID = UUID.fromString(promotionId);

			// Feign Client를 사용하여 사용자 정보 확인 (이미 존재하는 코드)
			ApiResponse<UserResponse> userApiResponse = userFeignClient.getUserByLoginId(loginId);

			if (userApiResponse == null || userApiResponse.data() == null) {
				throw new IllegalStateException("사용자 정보를 가져올 수 없습니다. loginId: " + loginId);
			}

			UserResponse user = userApiResponse.data();
			UUID userUUID = user.id();

			// 쿠폰 발급 처리 (loginId를 전달)
			CouponIssueResponseDto response = couponService.issueCoupon(promotionUUID, gender, userUUID, loginId);

			return ResponseEntity.ok(ApiResponse.of(BasicStatusCode.OK, response));
		} catch (Exception e) {
			log.error("쿠폰 발급 중 오류 발생: loginId={}, role={}, error={}", loginId, role, e.getMessage(), e);
			throw new IllegalStateException("쿠폰 발급 중 오류가 발생했습니다.", e);
		}
	}

	@PreAuthorize("hasAnyAuthority('MASTER')")
	@DeleteMapping("/{couponId}")
	public ResponseEntity<CouponUpdateResponseDto> deleteCoupon(
		@PathVariable UUID couponId,
		@RequestHeader(value = "X-User-Id") String userId // userId 추가
	) {
		CouponUpdateResponseDto response = couponService.deleteCoupon(couponId, userId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/promotion/{promotionId}/status")
	public ResponseEntity<CouponStatusResponseDto> getCouponStatus(@PathVariable UUID promotionId) {
		CouponStatusResponseDto response = couponService.getCouponStatus(promotionId);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyAuthority('MASTER')")
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

	@PreAuthorize("hasAnyAuthority('MEMBER', 'TEMPLE_ADMIN', 'MASTER')")
	@PostMapping("/{couponId}/transfer")
	public ResponseEntity<CouponTransferResponseDto> transferCoupon(
		@PathVariable UUID couponId,
		@RequestBody Map<String, String> request
	) {
		UUID toUserId = UUID.fromString(request.get("toUserId"));
		CouponTransferResponseDto response = couponService.transferCoupon(couponId, toUserId);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyAuthority('MASTER')")
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

	@PreAuthorize("hasAnyAuthority('MEMBER', 'TEMPLE_ADMIN', 'MASTER')")
	@GetMapping("/my-coupons")
	public ResponseEntity<Page<UserCoupon>> getMyCoupons(
		@RequestHeader("X-Login-Id") String loginId, // String으로 변경
		@RequestParam(value = "promotion", required = false) UUID promotionId,
		@RequestParam(value = "status", required = false) String status,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size
	) {
		// 본인 쿠폰 조회 서비스 호출
		Page<UserCoupon> userCoupons = couponService.getMyCoupons(loginId, promotionId, status, page, size);
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
