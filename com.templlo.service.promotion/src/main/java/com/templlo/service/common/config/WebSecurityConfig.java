package com.templlo.service.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.templlo.service.common.exception.GlobalFilterExceptionHandlerFilter;
import com.templlo.service.common.security.CustomAuthenticationFilter;
import com.templlo.service.common.security.UserRole;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {
	private final CustomAuthenticationFilter customAuthenticationFilter;
	private final GlobalFilterExceptionHandlerFilter globalFilterExceptionHandlerFilter;

	private final String USER = UserRole.MEMBER.name();
	private final String TEMPLE = UserRole.TEMPLE_ADMIN.name();
	private final String MASTER = UserRole.MASTER.name();

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.httpBasic(HttpBasicConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement((sessionManagement) ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			.authorizeHttpRequests((request) -> request
				// Promotion 경로 설정
				.requestMatchers("/api/promotions")
				.hasAuthority(MASTER) // 프로모션 생성
				.requestMatchers("/api/promotion/{promotionId}")
				.hasAuthority(MASTER) // 프로모션 수정, 삭제
				.requestMatchers("/api/promotions")
				.permitAll() // 모든 프로모션 조회 (ALL)
				.requestMatchers("/api/promotions/{promotionId}")
				.permitAll() // 특정 프로모션 조회 (ALL)
				.requestMatchers("/api/promotion/{promotionId}/participate")
				.hasAuthority(USER) // 특정 사용자의 프로모션 참여 (MEMBER)

				// Coupon 경로 설정
				.requestMatchers("/api/coupon/event/issue")
				.permitAll() // 이벤트 기반 쿠폰 발급 (ALL)
				.requestMatchers("/api/coupon/membership/issue")
				.hasAuthority(MASTER) // 멤버십 쿠폰 발급 (MASTER)
				.requestMatchers("/api/coupon/ticket/apply")
				.hasAuthority(USER) // 입장권 신청 (MEMBER)
				.requestMatchers("/api/coupon/ticket/issue")
				.hasAuthority(USER) // 입장권 발급 (MEMBER)
				.requestMatchers("/api/coupon/{couponId}/validate")
				.hasAuthority(MASTER) // 쿠폰 검증 (MASTER)
				.requestMatchers("/api/coupon/{couponId}/use")
				.hasAuthority(USER) // 쿠폰 사용 (MEMBER)
				.requestMatchers("/api/coupon/{couponId}/transfer")
				.hasAuthority(USER) // 쿠폰 양도 (MEMBER)
				.requestMatchers("/api/coupons")
				.permitAll() // 특정 회원의 쿠폰 조회 (ALL)
				.requestMatchers("/api/coupon/promotion/{promotionId}/status")
				.hasAuthority(MASTER) // 특정 프로모션의 쿠폰 발급 상태 조회 (MASTER)
				.requestMatchers("/api/coupon/{couponId}")
				.hasAuthority(MASTER) // 쿠폰 수정 및 삭제 (MASTER)
				.requestMatchers("/api/coupon/user/{userId}")
				.hasAuthority(MASTER) // 특정 사용자의 쿠폰 삭제 (MASTER)

				// 나머지 요청 기본 설정
				.anyRequest()
				.authenticated()); // 인증된 사용자만 접근 가능

		// filter 순서 : 안쪽부터 등록해야 함. 필터 순서는 바깥쪽부터 GlobalFilterExceptionHandlerFilter > CustomAuthenticationFilter > UsernamePasswordAuthenticationFilter
		http
			.addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(globalFilterExceptionHandlerFilter, CustomAuthenticationFilter.class)
		;

		http
			.formLogin(AbstractHttpConfigurer::disable)
		;

		return http.build();
	}
}