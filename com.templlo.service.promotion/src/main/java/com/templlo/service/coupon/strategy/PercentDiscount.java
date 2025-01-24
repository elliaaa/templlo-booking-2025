package com.templlo.service.coupon.strategy;

import com.templlo.service.coupon.entity.Coupon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@RequiredArgsConstructor
public class PercentDiscount implements DiscountStrategy {
    private final int percentage;

    @Override
    public int getDiscountPrice(int originalPrice) {
        log.info("originalPrice : {}, percentage: {},  (1 - (percentage/100.0f)) = {}",originalPrice, percentage,  (1 - (percentage/100.0f)));
        float floatPrice = originalPrice *  (1 - (percentage/100.0f));
        log.info("floatPrice : {}, (int) floatPrice = {}", originalPrice *  (1 - (percentage/100.0f)), (int) floatPrice);
        return (int) floatPrice;
    }
}
