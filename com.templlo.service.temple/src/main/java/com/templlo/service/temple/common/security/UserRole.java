package com.templlo.service.temple.common.security;

public enum UserRole {
	MEMBER("MEMBER"),
	TEMPLE_ADMIN("TEMPLE_ADMIN"),
	MASTER("MASTER");

	private final String authority;

	UserRole(String authority) {
		this.authority = authority;
	}

	public static UserRole fromString(String authority) {
		return UserRole.valueOf(authority);
	}
}
