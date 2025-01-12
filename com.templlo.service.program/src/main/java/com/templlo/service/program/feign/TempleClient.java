package com.templlo.service.program.feign;

import com.templlo.service.program.global.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "temple-service")
public interface TempleClient {
    @PreAuthorize("hasAuthority('TEMPLE_ADMIN')")
    @GetMapping("/api/temples/{templeId}/validate-admin")
    public ResponseEntity<ApiResponse<Void>> checkTempleOwnership(@PathVariable UUID templeId);

}
