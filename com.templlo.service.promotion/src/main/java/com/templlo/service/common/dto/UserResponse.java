package com.templlo.service.common.dto;

import java.util.UUID;

public record UserResponse(
	UUID id,
	String loginId,
	String email,
	String userName,
	String nickName,
	String gender,
	String birth,
	String role,
	String phone,
	int reviewCount
) {
}
