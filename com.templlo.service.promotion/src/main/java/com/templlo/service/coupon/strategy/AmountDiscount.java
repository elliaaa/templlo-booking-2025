package com.templlo.service.coupon.strategy;

import com.templlo.service.coupon.entity.Coupon;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AmountDiscount implements DiscountStrategy {
    private final int amount;

    @Override
    public int getDiscountPrice(int originalPrice) {
        return Math.max(0, originalPrice - amount);
    }
}
