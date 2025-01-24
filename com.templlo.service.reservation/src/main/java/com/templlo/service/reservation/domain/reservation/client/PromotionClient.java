package com.templlo.service.reservation.domain.reservation.client;

import com.templlo.service.reservation.domain.reservation.client.model.request.UseCouponReq;
import com.templlo.service.reservation.domain.reservation.client.model.response.UseCouponRes;
import com.templlo.service.reservation.global.feign.AuthHeader;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;


@FeignClient("promotion-service")
public interface PromotionClient {

    @AuthHeader
    @GetMapping("/api/coupons/{couponId}/use")
    UseCouponRes useCouponAndGetFinalPrice(
            @PathVariable(name = "couponId") UUID couponId,
            @RequestBody UseCouponReq useCouponReq);
}
