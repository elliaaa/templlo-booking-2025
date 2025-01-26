package com.templlo.service.program.kafka.consumer;

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

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationCanceledConsumer {

    private final ReservationCancelProducer reservationCancelProducer;
    private final ObjectMapper objectMapper;
    private final JpaProgramRepository jpaProgramRepository;
    private final JpaTempleStayDailyInfoRepository jpaTempleStayDailyInfoRepository;
    private final RedisTemplate<String, Object> redisTemplate;


    @Value("${spring.kafka.topics.reservation-cancel-confirmed}")
    private String reservationCancelConfirmedTopic;

    @DistributedLock(keyType = DistributedLockKey.PROGRAM_CAPACITY_PREFIX, idSpEL = "#message.programId()", maxWaitTime = 1000L)
    @KafkaListener(topics = "${spring.kafka.topics.reservation-canceled}", groupId = "reservation-canceled-program")
    public void consumeReservationCanceled(ReservationCancelMessage message) throws Exception {

        reservationCancelProducer.send(reservationCancelConfirmedTopic, processReservationCancel(message));

    }

    private ReservationConfirmMessage processReservationCancel(ReservationCancelMessage message) throws JsonProcessingException {

        Program program = jpaProgramRepository.findById(message.programId()).orElse(null);

        if (program == null) {
            return canceledReservationFailureMessage(message);
        }

//        // 예약일 검증
//        if (!program.getReservationStartDate().isBefore(LocalDate.now()) || program.getReservationEndDate().isAfter(LocalDate.now())) {
//            return canceledReservationFailureMessage(message);
//        }

        // 프로그램 타입에 따른 정원 증가 처리
        if (program.getType() == ProgramType.TEMPLE_STAY) {
            return increaseTempleStayAvailableCapacity(program, message);
        } else if (program.getType() == ProgramType.BLIND_DATE) {
            return increaseBlindDateAvailableCapacity(program, message);
        }

        return canceledReservationFailureMessage(message);
    }

    private ReservationConfirmMessage increaseBlindDateAvailableCapacity(Program program, ReservationCancelMessage message) throws JsonProcessingException {
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
                return canceledReservationFailureMessage(message);
            }
            redisTemplate.opsForValue().set(blindDateCacheKey, blindDateInfo, Duration.ofHours(1));
        }

        if (message.openType() == ReservationOpenType.ADDITIONAL_OPEN) {

            if (!blindDateInfo.getAdditionalReservationStartDate().isBefore(LocalDate.now()) || blindDateInfo.getAdditionalReservationEndDate().isBefore(LocalDate.now())) {
                return canceledReservationFailureMessage(message);
            }
        }

        blindDateInfo.increaseAvailableCapacity(message.gender());

        // Redis 캐시에 업데이트
        String updatedValue = objectMapper.writeValueAsString(blindDateInfo);
        redisTemplate.opsForValue().set(blindDateCacheKey, updatedValue, Duration.ofHours(1));

        return canceledReservationSuccessMessage(message);
    }

    private ReservationConfirmMessage increaseTempleStayAvailableCapacity(Program program, ReservationCancelMessage message) throws JsonProcessingException {
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
                return canceledReservationFailureMessage(message);
            }
            redisTemplate.opsForValue().set(templeStayCacheKey, objectMapper.writeValueAsString(templeStayDailyInfo), Duration.ofHours(1));
        }

        templeStayDailyInfo.increaseAvailableCapacity(message.amount());

        // Redis 캐시에 업데이트
        redisTemplate.opsForValue().set(templeStayCacheKey, objectMapper.writeValueAsString(templeStayDailyInfo), Duration.ofHours(1));

        return canceledReservationSuccessMessage(message);
    }

    private ReservationConfirmMessage canceledReservationFailureMessage(ReservationCancelMessage message) throws JsonProcessingException {
        String reservationKey = "canceledReservationId:" + message.reservationId();
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        // 실패 메시지 생성
        ReservationConfirmMessage reservationCancelMessage = ReservationConfirmMessage.FailureMessageFrom(message.reservationId());

        // Redis Hash에 데이터와 상태를 저장
        String bodyJson = objectMapper.writeValueAsString(reservationCancelMessage);
        hashOps.put(reservationKey, "body", bodyJson); // 메시지 데이터 저장
        hashOps.put(reservationKey, "status", ReservationConfirmMessageStatus.PENDING); // 상태 저장
        redisTemplate.expire(reservationKey, Duration.ofHours(1)); // TTL 설정

        return reservationCancelMessage;
    }

    private ReservationConfirmMessage canceledReservationSuccessMessage(ReservationCancelMessage message) throws JsonProcessingException {
        String reservationKey = "canceledReservationId:" + message.reservationId();
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        // 실패 메시지 생성
        ReservationConfirmMessage reservationCancelMessage = ReservationConfirmMessage.SuccessMessageFrom(message.reservationId());

        // Redis Hash에 데이터와 상태를 저장
        String bodyJson = objectMapper.writeValueAsString(reservationCancelMessage);
        hashOps.put(reservationKey, "body", bodyJson);; // 메시지 데이터 저장
        hashOps.put(reservationKey, "status", ReservationConfirmMessageStatus.PENDING); // 상태 저장
        redisTemplate.expire(reservationKey, Duration.ofHours(1)); // TTL 설정

        return reservationCancelMessage;
    }

}
