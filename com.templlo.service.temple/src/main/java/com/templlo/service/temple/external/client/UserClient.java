package com.templlo.service.temple.external.client;

import com.templlo.service.temple.external.config.FeignConfiguration;
import com.templlo.service.temple.external.dto.UserData;
import com.templlo.service.temple.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service",
             configuration = FeignConfiguration.class)
public interface UserClient {

    @GetMapping("/api/users/{loginId}")
    ApiResponse<UserData> getUserInfo(@PathVariable(name = "loginId") String loginId);
}
