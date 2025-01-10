package com.templlo.service.common.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "User AuthenticationFilter")
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String loginId = request.getHeader("X-Login-Id");
		String role = request.getHeader("X-User-Role");

		log.info("Request loginId : {} , role : {}", loginId, role);

		if (loginId != null && role != null) {
			UserDetailsImpl userDetails = new UserDetailsImpl(loginId, role);
			Authentication authentication = new PreAuthenticatedAuthenticationToken(
				userDetails, loginId, userDetails.getAuthorities()
			);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.debug("Gateway UserDetails - loginId: {}, role: {}", loginId, role);
		} else {
			log.error("로그인 ID 또는 역할 정보가 누락되었습니다. loginId: {}, role: {}", loginId, role);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 ID 또는 역할 정보가 필요합니다.");
			return;
		}

		filterChain.doFilter(request, response);
	}

}
