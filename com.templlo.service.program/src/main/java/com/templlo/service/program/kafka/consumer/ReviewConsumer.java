package com.templlo.service.program.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.templlo.service.program.entity.Program;
import com.templlo.service.program.exception.ProgramException;
import com.templlo.service.program.exception.ProgramStatusCode;
import com.templlo.service.program.kafka.message.review.ReviewCreateMessage;
import com.templlo.service.program.kafka.message.review.ReviewUpdateMessage;
import com.templlo.service.program.repository.JpaProgramRepository;
import com.templlo.service.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewConsumer {

    private final JpaProgramRepository jpaProgramRepository;
    private final ObjectMapper objectMapper;

    // 리뷰 생성 시 평점 계산
    @KafkaListener(topics = "${spring.kafka.topics.review-created}", groupId = "review-created-program")
    @Transactional
    public void consumeReviewCreated(String reviewCreatedMessage) throws JsonProcessingException {

        log.info("Consume Review Created Message start");

        ReviewCreateMessage message = objectMapper.readValue(reviewCreatedMessage, ReviewCreateMessage.class);

        Program program = jpaProgramRepository.findById(message.programId()).orElseThrow(
                () -> new ProgramException(ProgramStatusCode.PROGRAM_NOT_FOUND)
        );

        program.updateCalculateRating(message.rating(), program.getReviewCount());

        log.info("Consume Review Created Message end");

    }

    // 리뷰 별점 수정 시 평점 계산
    @KafkaListener(topics = "${spring.kafka.topics.review-updated}", groupId = "review-updated-program")
    @Transactional
    public void consumeReviewUpdated(String reviewUpdatedMessage) throws JsonProcessingException {

        log.info("Consume Review Updated Message start");

        ReviewUpdateMessage message = objectMapper.readValue(reviewUpdatedMessage, ReviewUpdateMessage.class);

        Program program = jpaProgramRepository.findById(message.programId()).orElseThrow(
                () -> new ProgramException(ProgramStatusCode.PROGRAM_NOT_FOUND)
        );

        program.updateReCalculateRating(message.newRating(),message.oldRating(), program.getReviewCount());

        log.info("Consume Review Updated Message start");
    }
}
