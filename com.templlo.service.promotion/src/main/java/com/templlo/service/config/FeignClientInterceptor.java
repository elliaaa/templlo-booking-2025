package com.templlo.service.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.templlo.service.common.security.UserDetailsImpl;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		log.info("FeignClientInterceptor 실행 중");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getPrincipal() == null) {
			log.error("Authentication 객체가 null입니다. 인증 정보 없음");
			throw new IllegalStateException("Feign 호출을 위한 인증 정보가 없습니다.");
		}

		UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
		log.info("X-Login-Id: {}", userDetails.getUsername());
		log.info("X-User-Role: {}", userDetails.getRole());

		template.header("X-Login-Id", userDetails.getUsername());
		template.header("X-User-Role", userDetails.getRole());
		log.info("Feign 요청에 헤더가 설정되었습니다. Template Headers: {}", template.headers());
	}
}
