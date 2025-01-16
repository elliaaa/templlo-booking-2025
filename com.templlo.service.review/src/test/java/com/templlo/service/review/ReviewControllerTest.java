package com.templlo.service.review;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.templlo.service.review.common.security.UserDetailsImpl;
import com.templlo.service.review.dto.CreateReviewRequestDto;
import com.templlo.service.review.service.CreateReviewService;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private CreateReviewService createReviewService;

	private UserDetailsImpl mockUserDetails;

	@Configuration
	static class TestConfig {
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapper();
		}

		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			http.csrf().disable()
				.authorizeHttpRequests().anyRequest().permitAll(); // 모든 요청 허용
			return http.build();
		}
	}

	@BeforeEach
	void setUp() {
		createReviewService = Mockito.mock(CreateReviewService.class);

		// Mock된 UserDetailsImpl 생성
		mockUserDetails = Mockito.mock(UserDetailsImpl.class);
		when(mockUserDetails.getLoginId()).thenReturn("testUser");

		// SecurityContext에 Mock UserDetails 설정
		SecurityContextHolder.setContext(new SecurityContextImpl());
		SecurityContextHolder.getContext()
			.setAuthentication(
				new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(mockUserDetails,
					null, mockUserDetails.getAuthorities()));
	}

	@Test
	void createReview_Success() throws Exception {
		// Given
		CreateReviewRequestDto requestDto = new CreateReviewRequestDto(
			UUID.randomUUID(),
			"이 프로그램은 정말 훌륭합니다. 매우 만족했습니다!",
			4.5
		);

		doNothing().when(createReviewService).createReview(any(CreateReviewRequestDto.class), eq("testUser"));

		// When & Then
		mockMvc.perform(post("/api/reviews")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("200"))
			.andExpect(jsonPath("$.code").value("OK"))
			.andExpect(jsonPath("$.message").value("Success"));

		verify(createReviewService, times(1)).createReview(any(CreateReviewRequestDto.class), eq("testUser"));
	}
}
