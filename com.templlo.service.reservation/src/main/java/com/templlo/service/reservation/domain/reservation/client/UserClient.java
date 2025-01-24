package com.templlo.service.reservation.domain.reservation.client;

import com.templlo.service.reservation.domain.reservation.client.model.response.GetUserByUserIdRes;
import com.templlo.service.reservation.domain.reservation.client.model.response.wrapper.UserServiceWrapperRes;
import com.templlo.service.reservation.global.feign.AuthHeader;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient("user-service")
public interface UserClient {

    @AuthHeader
    @GetMapping("/api/users/{userId}")
    UserServiceWrapperRes<GetUserByUserIdRes> getUser(@PathVariable(name = "userId") String userId);
}
