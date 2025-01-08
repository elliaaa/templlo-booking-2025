package com.templlo.service.coupon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.templlo.service.coupon.dto.TicketApplyRequestDto;
import com.templlo.service.coupon.dto.TicketApplyResponseDto;
import com.templlo.service.coupon.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coupon/ticket")
@RequiredArgsConstructor
public class TicketController {

	private final TicketService ticketService;

	@PostMapping("/apply")
	public ResponseEntity<TicketApplyResponseDto> applyForTicket(@RequestBody TicketApplyRequestDto requestDto) {
		TicketApplyResponseDto response = ticketService.applyForTicket(requestDto);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/issue")
	public ResponseEntity<?> issueTicket(@RequestBody String queueId) {
		ticketService.processQueue(queueId);
		return ResponseEntity.ok("입장권이 발급되었습니다.");
	}
}
