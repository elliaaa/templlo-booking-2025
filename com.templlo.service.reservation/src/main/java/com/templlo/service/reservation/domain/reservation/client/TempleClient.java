package com.templlo.service.reservation.domain.reservation.client;

import com.templlo.service.reservation.domain.reservation.client.model.response.TempleOwnerDataRes;
import com.templlo.service.reservation.domain.reservation.client.model.response.wrapper.TempleServiceWrapperRes;
import com.templlo.service.reservation.global.feign.AuthHeader;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient("temple-service")
public interface TempleClient {

    @AuthHeader
    @GetMapping("/{templeId}/validate-admin")
    TempleServiceWrapperRes<TempleOwnerDataRes> checkTempleOwnership(@PathVariable UUID templeId);
}
