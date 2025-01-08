package com.templlo.service.promotion.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	@PostMapping
	public ResponseEntity<PromotionResponseDto> createPromotion(
		@Valid @RequestBody PromotionRequestDto requestDto,
		@RequestHeader(value = "X-Login-Id", required = false) String userId,
		@RequestHeader(value = "X-User-Role", required = false) String role
	) {
		System.out.println("X-Login-Id: " + userId);
		System.out.println("X-User-Role: " + role);

		PromotionResponseDto responseDto = promotionService.createPromotion(requestDto, userId, role);
		return ResponseEntity.ok(responseDto);
	}

	@PatchMapping("/{promotionId}")
	public ResponseEntity<PromotionResponseDto> updatePromotion(
		@PathVariable UUID promotionId,
		@Valid @RequestBody PromotionUpdateDto updateDto) {
		PromotionResponseDto responseDto = promotionService.updatePromotion(promotionId, updateDto);
		return ResponseEntity.ok(responseDto);
	}

	@DeleteMapping("/{promotionId}")
	public ResponseEntity<PromotionResponseDto> deletePromotion(@PathVariable UUID promotionId) {
		PromotionResponseDto responseDto = promotionService.deletePromotion(promotionId);
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping
	public ResponseEntity<Page<PromotionDetailResponseDto>> getPromotions(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String type,
		@RequestParam(required = false) String status) {
		Page<PromotionDetailResponseDto> promotions = promotionService.getPromotions(page, size, type, status);
		return ResponseEntity.ok(promotions);
	}

	@GetMapping("/{promotionId}")
	public ResponseEntity<PromotionDetailResponseDto> getPromotion(@PathVariable UUID promotionId) {
		PromotionDetailResponseDto responseDto = promotionService.getPromotion(promotionId);
		return ResponseEntity.ok(responseDto);
	}
}
