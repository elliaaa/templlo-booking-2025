package com.templlo.service.reservation.domain.reservation.client.model.response;

import java.util.UUID;

public record GetUserByUserId(
        UUID id,
        String loginId,
        String email,
        String userName,
        String nickName,
        String gender,
        String birth,
        String role,
        String phone,
        int reviewCount,
        boolean isDeleted
) {
}
