package com.templlo.service.reservation.domain.temp;

import com.google.gson.Gson;
import com.templlo.service.reservation.domain.reservation.service.model.consume.CreateReservationResultConsume;
import com.templlo.service.reservation.domain.reservation.service.model.consume.CreateReservationResultStatus;
import com.templlo.service.reservation.global.common.response.ApiResponse;
import com.templlo.service.reservation.global.common.response.BasicStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.templlo.service.reservation.domain.reservation.service.model.consume.ConsumerTopicGroupName.TOPIC_RESERVATION_CONFIRM;

@RestController
@RequestMapping("api/test/kafka")
@RequiredArgsConstructor
public class KafkaTestController {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;

    @GetMapping("/create-reservation-result")
    public ResponseEntity<ApiResponse> tempCreateReservationMessageProducer(@RequestParam UUID reservationId) {
        CreateReservationResultConsume dto1 = CreateReservationResultConsume.builder()
                .reservationId(reservationId)
                .status(CreateReservationResultStatus.SUCCESS)
                .build();

        CreateReservationResultConsume dto2 = CreateReservationResultConsume.builder()
                .reservationId(reservationId)
                .status(CreateReservationResultStatus.FAILURE)
                .build();

        kafkaTemplate.send(TOPIC_RESERVATION_CONFIRM, null, gson.toJson(dto1));
        kafkaTemplate.send(TOPIC_RESERVATION_CONFIRM, null, gson.toJson(dto2));

        return ResponseEntity.ok().body(ApiResponse.of(BasicStatusCode.OK));
    }

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
