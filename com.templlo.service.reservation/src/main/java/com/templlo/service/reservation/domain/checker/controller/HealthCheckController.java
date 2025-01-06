package com.templlo.service.reservation.domain.checker.controller;

import com.templlo.service.reservation.global.common.response.ApiResponse;
import com.templlo.service.reservation.global.common.response.BasicStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checker")
public class HealthCheckController {

    /**
     * 서버 정상 작동 확인용 api
     * @return
     */
    @GetMapping("/health-check")
    public ResponseEntity<ApiResponse> healthCheck() {
        return ResponseEntity.ok().body(ApiResponse.of(BasicStatusCode.HEALTH_CHECK_OK));
    }
}
