package com.templlo.service.reservation.domain.checker.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerTestService {
    private final Gson gson;

    @KafkaListener(groupId = "group1", topics = "topicA")
    public void consumeTopicAFromGroup1(String message) {
        log.info("consume : group1 : topic A : message : {}", message);
    }

    @KafkaListener(groupId = "group2", topics = "topicA")
    public void consumeTopicAFromGroup2(String message) {
        log.info("consume : group2 : topic A : message : {}", message);
    }

    @KafkaListener(groupId = "group3", topics = "topicC")
    public void consumeTopicCFromGroup3(String message) {
        log.info("consume : group3 : topic C : message : {}", message);
    }

    @KafkaListener(groupId = "group3", topics = "topicD")
    public void consumeTopicDFromGroup3(String message) {
        log.info("consume : group3 : topic D : message : {}", message);
    }

    @KafkaListener(groupId = "group5", topics = "topicE")
    public void consumeTopicEFromGroup5(String message) {
        log.info("consume : group5 : topic E : message : {}", message);
    }

    @KafkaListener(groupId = "group6", topics = "topicF")
    public void consumeTopicFFromGroup6(String message) {
        log.info("consume : group6 : topic F : message : {}", message);

        try{
            TestReq dto = gson.fromJson(message, TestReq.class);
            log.info("consume : group6 : topic F : dto : {}", dto);
        } catch (Exception e) {
            log.info("json deserialize error : {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
