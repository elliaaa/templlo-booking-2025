package com.templlo.service.temple.controller;

import com.templlo.service.temple.Service.TempleSearchService;
import com.templlo.service.temple.Service.TempleService;
import com.templlo.service.temple.common.security.UserDetailsImpl;
import com.templlo.service.temple.dto.CreateTempleRequest;
import com.templlo.service.temple.dto.TempleResponse;
import com.templlo.service.temple.dto.UpdateTempleRequest;
import com.templlo.service.temple.common.response.ApiResponse;
import com.templlo.service.temple.common.response.BasicStatusCode;
import com.templlo.service.temple.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/temples")
@RequiredArgsConstructor
public class TempleController {

    private final TempleService templeService;
    private final TempleSearchService templeSearchService;

    @PreAuthorize("hasAuthority('TEMPLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<TempleResponse>> createTemple(@RequestBody CreateTempleRequest request,
                                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {

        TempleResponse response = templeService.createTemple(request,userDetails.getLoginId());
        return ResponseEntity.ok(ApiResponse.of(BasicStatusCode.OK, response));
    }

    @PreAuthorize("hasAuthority('TEMPLE_ADMIN')")
    @PatchMapping("/{templeId}")
    public ResponseEntity<ApiResponse<TempleResponse>> updateTemple(@PathVariable UUID templeId,
                                                                    @RequestBody UpdateTempleRequest request,
                                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {

        TempleResponse response = templeService.updateTemple(templeId, request,userDetails.getLoginId());
        return ResponseEntity.ok(ApiResponse.of(BasicStatusCode.OK, response));
    }

    @PreAuthorize("hasAuthority('MASTER')")
    @DeleteMapping("/{templeId}")
    public ResponseEntity<ApiResponse<Void>> deleteTemple(@PathVariable UUID templeId,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        templeService.deleteTemple(templeId,userDetails.getLoginId());
        return ResponseEntity.ok(ApiResponse.of(BasicStatusCode.OK, null));
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

}
