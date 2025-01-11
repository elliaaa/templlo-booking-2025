package com.templlo.service.review.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.templlo.service.review.common.exception.baseException.DuplicatedReviewException;
import com.templlo.service.review.common.exception.baseException.NonReviewableException;
import com.templlo.service.review.common.response.ApiResponse;
import com.templlo.service.review.dto.CreateReviewRequestDto;
import com.templlo.service.review.entity.Review;
import com.templlo.service.review.external.feignClient.client.ReservationClient;
import com.templlo.service.review.external.feignClient.client.UserClient;
import com.templlo.service.review.external.feignClient.dto.ReservationData;
import com.templlo.service.review.external.feignClient.dto.ReservationStatus;
import com.templlo.service.review.external.feignClient.dto.UserData;
import com.templlo.service.review.external.kafka.producer.dto.ReviewCreatedEventDto;
import com.templlo.service.review.external.kafka.producer.ReviewEventProducer;
import com.templlo.service.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Create Review Service")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateReviewService {

	private final ReviewRepository reviewRepository;
	private final UserClient userClient;
	private final ReservationClient reservationClient;
	private final ReviewEventProducer eventProducer;

	@Transactional
	public void createReview(CreateReviewRequestDto request, String loginId) {
		ApiResponse<UserData> userResponse = userClient.getUserInfo(loginId);
		UUID userId = userResponse.data().id();
		validateDuplicatedReview(request, userId);

		ResponseEntity<ApiResponse<ReservationData>> reserveResponse = reservationClient.getReservationInfo(request.reservationId());
		validateReservationStatus(reserveResponse);

		UUID reservationId = reserveResponse.getBody().data().reservationId();
		UUID programId = reserveResponse.getBody().data().programId();

		Review review = Review.create(reservationId, programId, userId, request.rating(), request.content());
		reviewRepository.save(review);

		// TODO 트랜잭션이 순차적으로 진행되는건지 확인이 필요(트러블슈팅 사항 : DB 트랜잭션이 종료 되기전에 이벤트가 발행된다면?) #1
		ReviewCreatedEventDto eventDto = ReviewCreatedEventDto.of(loginId, review);
		eventProducer.publishReviewCreated(eventDto);
	}

	private static void validateReservationStatus(ResponseEntity<ApiResponse<ReservationData>> reserveResponse) {
		if (!ReservationStatus.COMPLETED.equals(reserveResponse.getBody().data().status())) {
			throw new NonReviewableException();
		}
	}

	private void validateDuplicatedReview(CreateReviewRequestDto request, UUID userId) {
		reviewRepository.findByUserIdAndReservationId(userId, request.reservationId())
			.ifPresent(review -> {
				throw new DuplicatedReviewException();
			});
	}

}
