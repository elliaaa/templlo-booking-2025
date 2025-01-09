package com.templlo.service.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.templlo.service.common.dto.UserResponse;
import com.templlo.service.common.response.ApiResponse;

@FeignClient(name = "user-service", url = "http://localhost:19010")
public interface UserFeignClient {
	@GetMapping("/api/users/{loginId}")
	ApiResponse<UserResponse> getUserByLoginId(@PathVariable("loginId") String loginId);
}



