package com.templlo.service.reservation.global.common.jpa;

import jakarta.persistence.PreUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomAuditingEntityListener extends AuditingEntityListener {

    @PreUpdate
    @Override
    public void touchForUpdate(Object target) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }
        super.touchForUpdate(target);
    }
}
