package com.templlo.service.coupon.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.templlo.service.coupon.dto.TicketApplyRequestDto;
import com.templlo.service.coupon.dto.TicketApplyResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

	private final KafkaProducerService kafkaProducerService;
	private final AtomicInteger queueCounter = new AtomicInteger(0);

	public TicketApplyResponseDto applyForTicket(TicketApplyRequestDto requestDto) {
		int queuePosition = queueCounter.incrementAndGet();
		kafkaProducerService.sendToQueue(requestDto);
		return new TicketApplyResponseDto("WAITING", queuePosition, "대기열에 등록되었습니다.");
	}

	public void processQueue(String queueId) {
		// 대기열 순서대로 발급 처리 로직
		System.out.println("Issuing ticket for queue ID: " + queueId);
	}
}
