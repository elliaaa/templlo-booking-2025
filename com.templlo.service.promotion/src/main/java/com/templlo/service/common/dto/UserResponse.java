package com.templlo.service.common.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) // 불필요한 필드 무시
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
