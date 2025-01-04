package com.templlo.service.promotion.controller;

import com.templlo.service.promotion.dto.PromotionRequestDto;
import com.templlo.service.promotion.dto.PromotionResponseDto;
import com.templlo.service.promotion.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promotion")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping
    public ResponseEntity<PromotionResponseDto> createPromotion(@RequestBody PromotionRequestDto requestDto) {
        PromotionResponseDto responseDto = promotionService.createPromotion(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
