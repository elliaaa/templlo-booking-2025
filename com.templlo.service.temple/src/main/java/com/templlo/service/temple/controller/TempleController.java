package com.templlo.service.temple.controller;

import com.templlo.service.temple.Service.TempleSearchService;
import com.templlo.service.temple.Service.TempleService;
import com.templlo.service.temple.dto.CreateTempleRequest;
import com.templlo.service.temple.dto.TempleResponse;
import com.templlo.service.temple.dto.UpdateTempleRequest;
import com.templlo.service.temple.global.response.ApiResponse;
import com.templlo.service.temple.global.response.BasicStatusCode;
import com.templlo.service.temple.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/temples")
@RequiredArgsConstructor
public class TempleController {

    private final TempleService templeService;
    private final TempleSearchService templeSearchService;

    @PostMapping
    public ResponseEntity<ApiResponse<TempleResponse>> createTemple(@RequestBody CreateTempleRequest request) {

        TempleResponse response = templeService.createTemple(request);
        return ResponseEntity.ok(ApiResponse.of(BasicStatusCode.OK, response));
    }

    @GetMapping("/region")
    public ResponseEntity<ApiResponse<PageResponse<TempleResponse>>> getTemplesByRegion(@RequestParam String region,
                                                                                        Pageable pageable) {

        PageResponse<TempleResponse> responses = templeService.getTemplesByRegion(region, pageable);
        return ResponseEntity.ok(ApiResponse.of(BasicStatusCode.OK, responses));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<TempleResponse>>> searchTemples(@RequestParam String keyword,
                                                                                   @PageableDefault(size = 10) Pageable pageable) {
        PageResponse<TempleResponse> responses = templeSearchService.searchTemples(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.of(BasicStatusCode.OK, responses));
    }


    @PatchMapping("/{templeId}")
    public ResponseEntity<ApiResponse<TempleResponse>> updateTemple(@PathVariable UUID templeId,
                                                                    @RequestBody UpdateTempleRequest request) {

        TempleResponse response = templeService.updateTemple(templeId, request);
        return ResponseEntity.ok(ApiResponse.of(BasicStatusCode.OK, response));
    }

    @DeleteMapping("/{templeId}")
    public ResponseEntity<ApiResponse<Void>> deleteTemple(@PathVariable UUID templeId) {
        templeService.deleteTemple(templeId);
        return ResponseEntity.ok(ApiResponse.of(BasicStatusCode.OK, null));
    }
}
