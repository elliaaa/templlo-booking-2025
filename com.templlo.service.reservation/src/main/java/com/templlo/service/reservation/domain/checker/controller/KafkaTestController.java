package com.templlo.service.reservation.domain.checker.controller;

import com.google.gson.Gson;
import com.templlo.service.reservation.global.common.response.ApiResponse;
import com.templlo.service.reservation.global.common.response.BasicStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/test/kafka")
@RequiredArgsConstructor
public class KafkaTestController {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;

    @GetMapping("/t1")
    public ResponseEntity<ApiResponse> produceMessage() {

        for(int i=0; i<3; i++){
            kafkaTemplate.send("topicA", null, "message "+i);
        }

        for(int i=-1; -4<i; i--){
            kafkaTemplate.send("topicB", null, "message "+i);
        }

        return ResponseEntity.ok().body(ApiResponse.of(BasicStatusCode.OK));
    }

    @GetMapping("/t2")
    public ResponseEntity<ApiResponse> produceMessageInput(
            @RequestParam String topic,
            @RequestParam String key,
            @RequestBody TestReq dto
    ) {
        String dtoStr = gson.toJson(dto);
        kafkaTemplate.send(topic, key, dtoStr);

        return ResponseEntity.ok().body(ApiResponse.of(BasicStatusCode.OK));
    }
}
