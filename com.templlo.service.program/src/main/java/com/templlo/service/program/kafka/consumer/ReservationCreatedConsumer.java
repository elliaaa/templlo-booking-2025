package com.templlo.service.program.kafka.consumer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.templlo.service.program.entity.*;
import com.templlo.service.program.exception.ProgramException;
import com.templlo.service.program.exception.ProgramStatusCode;
import com.templlo.service.program.global.aop.distributed_lock.DistributedLock;
import com.templlo.service.program.global.aop.distributed_lock.DistributedLockKey;
import com.templlo.service.program.kafka.message.reservation.*;
import com.templlo.service.program.kafka.producer.ReservationCancelProducer;
import com.templlo.service.program.kafka.producer.ReservationConfirmProducer;
import com.templlo.service.program.repository.JpaProgramRepository;
import com.templlo.service.program.repository.JpaTempleStayDailyInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationCreatedConsumer {

    private final ReservationConfirmProducer reservationConfirmProducer;
    private final ObjectMapper objectMapper;
    private final JpaProgramRepository jpaProgramRepository;
    private final JpaTempleStayDailyInfoRepository jpaTempleStayDailyInfoRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.kafka.topics.reservation-confirmed}")
    private String reservationConfirmedTopic;


    @DistributedLock(keyType = DistributedLockKey.PROGRAM_CAPACITY_PREFIX, idSpEL = "#message.programId()", maxWaitTime = 1000L)
    @KafkaListener(topics = "${spring.kafka.topics.reservation-created}", groupId = "reservation-created-program")
    public void consumeReservationCreated(ReservationCreateMessage message) throws Exception {

        reservationConfirmProducer.send(reservationConfirmedTopic, processReservationConfirm(message));

    }

    private ReservationConfirmMessage processReservationConfirm(ReservationCreateMessage message) throws IOException {

        Program program = jpaProgramRepository.findById(message.programId()).orElse(null);

        if (program == null) {
            return createdReservationFailureMessage(message);
        }

        // 예약일 검증
//        if (!program.getReservationStartDate().isBefore(LocalDate.now()) || program.getReservationEndDate().isAfter(LocalDate.now())) {
//            return createdReservationFailureMessage(message);
//        }

        // 프로그램 타입에 따른 정원 감소 처리
        if (program.getType() == ProgramType.TEMPLE_STAY) {
            return reduceTempleStayAvailableCapacity(program, message);
        } else if (program.getType() == ProgramType.BLIND_DATE) {
            return reduceBlindDateAvailableCapacity(program, message);
        }

        return createdReservationFailureMessage(message);
    }


    private ReservationConfirmMessage reduceTempleStayAvailableCapacity(Program program, ReservationCreateMessage message) throws JsonProcessingException {
        String templeStayCacheKey = "templeStayInfo:" + message.programId() + "date:" + message.programDate();

        // Redis 캐시에서 TempleStay 정보 조회
        Object rawValue = redisTemplate.opsForValue().get(templeStayCacheKey);

        TempleStayDailyInfo templeStayDailyInfo = null;
        if (rawValue instanceof LinkedHashMap) {
            templeStayDailyInfo = objectMapper.convertValue(rawValue, TempleStayDailyInfo.class);
        }
        // 캐시 데이터가 없으면 DB에서 조회 후 캐싱
        if (templeStayDailyInfo == null) {
            templeStayDailyInfo = jpaTempleStayDailyInfoRepository.findByProgram_IdAndProgramDate(program.getId(), message.programDate())
                    .orElse(null);

            if (templeStayDailyInfo == null) {
                return createdReservationFailureMessage(message);
            }
            redisTemplate.opsForValue().set(templeStayCacheKey, objectMapper.writeValueAsString(templeStayDailyInfo), Duration.ofHours(1));
        }

        if (templeStayDailyInfo.getStatus() != ProgramStatus.ACTIVE) {
            return createdReservationFailureMessage(message);
        }

        templeStayDailyInfo.reduceAvailableCapacity(message.amount());

        // Redis 캐시에 업데이트
        redisTemplate.opsForValue().set(templeStayCacheKey, objectMapper.writeValueAsString(templeStayDailyInfo), Duration.ofHours(1));

        return createdReservationSuccessMessage(message);
    }

    private ReservationConfirmMessage reduceBlindDateAvailableCapacity(Program program, ReservationCreateMessage message) throws IOException {
        String blindDateCacheKey = "blindDateInfo:" + message.programId() + "date:" + message.programDate();

        Object rawValue = redisTemplate.opsForValue().get(blindDateCacheKey);
        BlindDateInfo blindDateInfo = null;
        if (rawValue instanceof LinkedHashMap) {
            blindDateInfo = objectMapper.convertValue(rawValue, BlindDateInfo.class);
        }

        // 캐시 데이터가 없으면 DB에서 조회 후 캐싱
        if (blindDateInfo == null) {
            blindDateInfo = program.getBlindDateInfo();
            if (blindDateInfo == null) {
                return createdReservationFailureMessage(message);
            }
            redisTemplate.opsForValue().set(blindDateCacheKey, objectMapper.writeValueAsString(blindDateInfo), Duration.ofHours(1));
        }

        if (message.openType() == ReservationOpenType.ADDITIONAL_OPEN) {

            if (!blindDateInfo.getAdditionalReservationStartDate().isBefore(LocalDate.now()) || blindDateInfo.getAdditionalReservationEndDate().isBefore(LocalDate.now())) {
                return createdReservationFailureMessage(message);
            }
        }

        if ((message.gender() == Gender.MALE && blindDateInfo.getGenderStatus() == ProgramGenderStatus.MALE_CLOSED)
                || (message.gender() == Gender.FEMALE && blindDateInfo.getGenderStatus() == ProgramGenderStatus.FEMALE_CLOSED)
                || blindDateInfo.getStatus() != ProgramStatus.ACTIVE) {

            return createdReservationFailureMessage(message);
        }

        blindDateInfo.reduceAvailableCapacity(message.gender());

        // Redis 캐시에 업데이트
        String updatedValue = objectMapper.writeValueAsString(blindDateInfo);
        redisTemplate.opsForValue().set(blindDateCacheKey, updatedValue, Duration.ofHours(1));

        return createdReservationSuccessMessage(message);
    }

    private ReservationConfirmMessage createdReservationFailureMessage(ReservationCreateMessage message) throws JsonProcessingException {
        String reservationConfirmKey = "createdReservationId:" + message.reservationId();
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        // 실패 메시지 생성
        ReservationConfirmMessage reservationConfirmMessage = ReservationConfirmMessage.FailureMessageFrom(message.reservationId());

        // Redis Hash에 데이터와 상태를 저장
        String bodyJson = objectMapper.writeValueAsString(reservationConfirmMessage);
        hashOps.put(reservationConfirmKey, "body", bodyJson); // 메시지 데이터 저장
        hashOps.put(reservationConfirmKey, "status", ReservationConfirmMessageStatus.PENDING); // 상태 저장
        redisTemplate.expire(reservationConfirmKey, Duration.ofHours(1)); // TTL 설정

        return reservationConfirmMessage;
    }

    private ReservationConfirmMessage createdReservationSuccessMessage(ReservationCreateMessage message) throws JsonProcessingException {
        String reservationConfirmKey = "createdReservationId:" + message.reservationId();
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        // 실패 메시지 생성
        ReservationConfirmMessage reservationConfirmMessage = ReservationConfirmMessage.SuccessMessageFrom(message.reservationId());

        // Redis Hash에 데이터와 상태를 저장
        String bodyJson = objectMapper.writeValueAsString(reservationConfirmMessage);
        hashOps.put(reservationConfirmKey, "body", bodyJson);; // 메시지 데이터 저장
        hashOps.put(reservationConfirmKey, "status", ReservationConfirmMessageStatus.PENDING); // 상태 저장
        redisTemplate.expire(reservationConfirmKey, Duration.ofHours(1)); // TTL 설정

        return reservationConfirmMessage;
    }


}