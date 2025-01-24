package com.templlo.service.coupon.strategy;

import com.templlo.service.coupon.entity.Coupon;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface DiscountStrategy {
    int getDiscountPrice(int originalPrice);
}
