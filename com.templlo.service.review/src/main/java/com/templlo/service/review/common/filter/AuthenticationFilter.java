package com.templlo.service.review.common.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.templlo.service.review.common.security.UserDetailsImpl;

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
		String token = request.getHeader("X-Token");

		log.info("Request loginId : {} , role : {}", loginId, role);

		if (loginId != null && role != null) {
			UserDetailsImpl userDetails = new UserDetailsImpl(loginId, role, token);
			Authentication authentication = new PreAuthenticatedAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);

			log.debug("Gateway UserDetails - loginId: {}, role: {}", loginId, role);
		}

		filterChain.doFilter(request, response);
	}

}
