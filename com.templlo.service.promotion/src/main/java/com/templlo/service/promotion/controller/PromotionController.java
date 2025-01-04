package com.templlo.service.promotion.controller;

import com.templlo.service.promotion.dto.PromotionRequestDto;
import com.templlo.service.promotion.dto.PromotionResponseDto;
import com.templlo.service.promotion.dto.PromotionUpdateDto;
import com.templlo.service.promotion.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/promotion")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping
    public ResponseEntity<PromotionResponseDto> createPromotion(@Valid @RequestBody PromotionRequestDto requestDto) {
        PromotionResponseDto responseDto = promotionService.createPromotion(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{promotionId}")
    public ResponseEntity<PromotionResponseDto> updatePromotion(
            @PathVariable UUID promotionId,
            @RequestBody PromotionUpdateDto updateDto) {
        PromotionResponseDto responseDto = promotionService.updatePromotion(promotionId, updateDto);
        return ResponseEntity.ok(responseDto);
    }
}
