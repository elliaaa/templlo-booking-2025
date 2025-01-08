package com.templlo.service.reservation.domain.checker.controller;

import com.templlo.service.reservation.domain.reservation.controller.exception.ReservationStatusCode;
import com.templlo.service.reservation.domain.reservation.controller.exception.ReservationException;
import com.templlo.service.reservation.global.common.response.ApiResponse;
import com.templlo.service.reservation.global.common.response.BasicStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 테스트용 임시로 만든 엔드포인트들
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    /**
     * GlobalExceptionHandler 테스트를 위한 예외 발생 api
     * @return 예약 id 를 찾을 수 없다는 에러 발생
     */
    @GetMapping("/reservation-exception")
    public ResponseEntity<ApiResponse> reservationExceptionTest() {
        throw new ReservationException(ReservationStatusCode.RESERVATION_NOT_FOUND);
    }


    /**
     * 시큐리티 접근이 제한되어있는 api
     * (미인증 사용자는 403 Forbidden 응답이 와야 정상)
     * @return
     */
    @GetMapping("/need-auth")
    public ResponseEntity<ApiResponse> needAuthTest() {
        return ResponseEntity.ok().body(ApiResponse.of(BasicStatusCode.OK));
    }

    /**
     * 권한 test : USER 권한 허용 api
     * @return
     */
    @GetMapping("/auth/user")
    public ResponseEntity<ApiResponse> testRoleUser(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok().body(ApiResponse.of(BasicStatusCode.OK, userId));
    }

    /**
     * 권한 test : TEMPLE_ADMIN 권한 허용 api
     * @return
     */
    @GetMapping("/auth/temple")
    public ResponseEntity<ApiResponse> testRoleTemple(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok().body(ApiResponse.of(BasicStatusCode.OK, userId));
    }

    /**
     * 권한 test : MASTER 권한 허용 api
     * @return
     */
    @PreAuthorize("hasAuthority('MASTER')") // 이렇게도 권한 설정 가능
    @GetMapping("/auth/master")
    public ResponseEntity<ApiResponse> testRoleMaster(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok().body(ApiResponse.of(BasicStatusCode.OK, userId));
    }
}
