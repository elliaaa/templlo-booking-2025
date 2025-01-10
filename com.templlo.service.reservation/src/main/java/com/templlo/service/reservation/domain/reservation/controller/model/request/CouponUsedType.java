package com.templlo.service.reservation.domain.reservation.controller.model.request;

import lombok.Getter;

@Getter
public enum CouponUsedType {
    USED(true),
    NOT_USED(false)
    ;

    private final boolean isUsed;

    CouponUsedType(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public static CouponUsedType valueOfIsUsed(boolean isUsed) {
        for (CouponUsedType e : CouponUsedType.values()) {
            if (e.isUsed == isUsed) {
                return e;
            }
        }
        return null;
    }
}
