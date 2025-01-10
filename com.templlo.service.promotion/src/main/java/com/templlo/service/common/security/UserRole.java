package com.templlo.service.common.security;

public enum UserRole {
	MEMBER("MEMBER"),
	TEMPLE_ADMIN("TEMPLE_ADMIN"),
	MASTER("MASTER");

	private final String authority;

	UserRole(String authority) {
		this.authority = authority;
	}

	public static UserRole fromString(String authority) {
		for (UserRole role : UserRole.values()) {
			if (role.authority.equalsIgnoreCase(authority)) {
				return role;
			}
		}
		throw new IllegalArgumentException("Unknown authority: " + authority);
	}
}
