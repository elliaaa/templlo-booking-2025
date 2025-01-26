package com.templlo.service.reservation.domain.reservation.service;

import static com.templlo.service.reservation.domain.reservation.service.model.consume.ConsumerTopicGroupName.*;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.templlo.service.reservation.domain.reservation.client.PromotionClient;
import com.templlo.service.reservation.domain.reservation.controller.exception.ReservationException;
import com.templlo.service.reservation.domain.reservation.controller.exception.ReservationStatusCode;
import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import com.templlo.service.reservation.domain.reservation.repository.ReservationRepository;
import com.templlo.service.reservation.domain.reservation.service.model.consume.CancelReservationResultConsume;
import com.templlo.service.reservation.domain.reservation.service.model.consume.CreateReservationResultConsume;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationConsumeService {
	private final ReservationRepository reservationRepository;
	private final PromotionClient promotionClient;
	private final Gson gson;

	@KafkaListener(groupId = GROUP_RESERVATION_CONFIRM, topics = TOPIC_RESERVATION_CONFIRM)
	@Transactional
	public void createReservationResultConsumer(String messageStr) {
		CreateReservationResultConsume message = gson.fromJson(messageStr, CreateReservationResultConsume.class);

		Reservation reservation = getReservation(message.reservationId());

		switch (message.status()) {
			case SUCCESS -> reservation.updateStatusCompleted();
			case FAILURE -> {
				reservation.updateStatusFailed();

				if (reservation.getCouponId() != null) {
					resetCouponStatus(reservation.getCouponId());
				}
			}
		}
	}

	@KafkaListener(groupId = GROUP_RESERVATION_CANCEL_CONFIRM, topics = TOPIC_RESERVATION_CANCEL_CONFIRM)
	@Transactional
	public void cancelReservationResultConsumer(String messageStr) {
		CancelReservationResultConsume message = gson.fromJson(messageStr, CancelReservationResultConsume.class);

		Reservation reservation = getReservation(message.reservationId());

		switch (message.status()) {
			case SUCCESS -> reservation.updateStatusCanceledOrRejected();
			case FAILURE -> reservation.updateStatusCanceledOrRejectedFailed();
		}
	}

	private Reservation getReservation(UUID message) {
		return reservationRepository.findById(message).orElseThrow(
			() -> new ReservationException(ReservationStatusCode.RESERVATION_NOT_FOUND));
	}

	private void resetCouponStatus(UUID couponId) {
		try {
			promotionClient.resetCoupon(couponId);
		} catch (Exception e) {
			throw new ReservationException(ReservationStatusCode.COUPON_RESET_FAILED);
		}
	}
}