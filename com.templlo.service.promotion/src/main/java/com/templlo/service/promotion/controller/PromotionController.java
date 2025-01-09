package com.templlo.service.promotion.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.templlo.service.common.response.ApiResponse;
import com.templlo.service.common.response.BasicStatusCode;
import com.templlo.service.common.response.PageResponse;
import com.templlo.service.common.security.UserDetailsImpl;
import com.templlo.service.promotion.dto.PromotionDetailResponseDto;
import com.templlo.service.promotion.dto.PromotionRequestDto;
import com.templlo.service.promotion.dto.PromotionResponseDto;
import com.templlo.service.promotion.dto.PromotionUpdateDto;
import com.templlo.service.promotion.service.PromotionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

	private final PromotionService promotionService;

	@PreAuthorize("hasAuthority('MASTER')")
	@PostMapping
	public ResponseEntity<ApiResponse<PromotionResponseDto>> createPromotion(
		@Valid @RequestBody PromotionRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		// 로그인 ID와 역할을 UserDetailsImpl에서 가져옴
		String userId = userDetails.getLoginId();
		String role = userDetails.getRole();

		PromotionResponseDto responseDto = promotionService.createPromotion(requestDto, userId, role);
		return ResponseEntity.ok(ApiResponse.of(BasicStatusCode.OK, responseDto));
	}

	@PreAuthorize("hasAuthority('MASTER')")
	@PatchMapping("/{promotionId}")
	public ResponseEntity<ApiResponse<PromotionResponseDto>> updatePromotion(
		@PathVariable UUID promotionId,
		@Valid @RequestBody PromotionUpdateDto updateDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails // Spring Security에서 인증된 사용자 정보 가져오기
	) {
		// 로그인 ID를 가져옴
		String userId = userDetails.getLoginId();

		// 서비스 호출
		PromotionResponseDto responseDto = promotionService.updatePromotion(promotionId, updateDto, userId);

		// 응답 반환
		return ResponseEntity.ok(ApiResponse.of(BasicStatusCode.OK, responseDto));
	}

	@PreAuthorize("hasAuthority('MASTER')")
	@DeleteMapping("/{promotionId}")
	public ResponseEntity<ApiResponse<PromotionResponseDto>> deletePromotion(
		@PathVariable UUID promotionId,
		@AuthenticationPrincipal UserDetailsImpl userDetails // Spring Security에서 인증된 사용자 정보 가져오기
	) {
		// 로그인 ID를 가져옴
		String userId = userDetails.getLoginId();

		// 서비스 호출
		PromotionResponseDto responseDto = promotionService.deletePromotion(promotionId, userId);

		// 응답 반환
		return ResponseEntity.ok(ApiResponse.of(BasicStatusCode.OK, responseDto));
	}

	@PreAuthorize("hasAnyAuthority('MEMBER', 'TEMPLE_ADMIN', 'MASTER')")
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<PromotionDetailResponseDto>>> getPromotions(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String type,
		@RequestParam(required = false) String status
	) {
		// 프로모션 목록 조회 서비스 호출
		PageResponse<PromotionDetailResponseDto> promotions = promotionService.getPromotions(page, size, type, status);

		// 성공 응답 반환
		return ResponseEntity.ok(ApiResponse.of(BasicStatusCode.OK, promotions));
	}

	@PreAuthorize("hasAnyAuthority('MEMBER', 'TEMPLE_ADMIN', 'MASTER')")
	@GetMapping("/{promotionId}")
	public ResponseEntity<ApiResponse<PromotionDetailResponseDto>> getPromotion(@PathVariable UUID promotionId) {
		// 특정 프로모션 조회 서비스 호출
		PromotionDetailResponseDto responseDto = promotionService.getPromotion(promotionId);

		// 성공 응답 반환
		return ResponseEntity.ok(ApiResponse.of(BasicStatusCode.OK, responseDto));
	}

}
