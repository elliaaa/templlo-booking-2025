package com.templlo.service.common.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

	private static final ThreadLocal<String> CURRENT_USER = new ThreadLocal<>();

	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
			.map(authentication -> authentication.getName()); // 로그인된 유저 ID 반환
	}

	/**
	 * Kafka 헤더 또는 다른 외부 요청에서 받은 사용자 ID를 설정
	 */
	public static void setCurrentUser(String userId) {
		CURRENT_USER.set(userId);
	}

	/**
	 * 현재 스레드에서 사용자 ID 제거
	 */
	public static void clearCurrentUser() {
		CURRENT_USER.remove();
	}
}
