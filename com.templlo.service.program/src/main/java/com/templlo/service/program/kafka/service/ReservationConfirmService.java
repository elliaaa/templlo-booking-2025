package com.templlo.service.program.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.templlo.service.program.entity.*;
import com.templlo.service.program.exception.ProgramException;
import com.templlo.service.program.exception.ProgramStatusCode;
import com.templlo.service.program.kafka.message.reservation.Gender;
import com.templlo.service.program.kafka.message.reservation.ReservationConfirmMessage;
import com.templlo.service.program.kafka.message.reservation.ReservationConfirmMessageStatus;
import com.templlo.service.program.kafka.message.reservation.ReservationCreateMessage;
import com.templlo.service.program.repository.JpaProgramRepository;
import com.templlo.service.program.repository.JpaTempleStayDailyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationConfirmService {

    private final JpaProgramRepository jpaProgramRepository;
    private final JpaTempleStayDailyInfoRepository jpaTempleStayDailyInfoRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public ReservationConfirmMessage processReservation(ReservationCreateMessage message) throws JsonProcessingException {

        // 프로그램 조회 (Redis 캐시 우선 조회)
        Program program = jpaProgramRepository.findById(message.programId()).orElse(null);

        if (program == null) {
            return createFailureMessage(message);
        }

        // 예약일 검증
        if (!program.getReservationStartDate().isBefore(LocalDate.now()) || program.getReservationEndDate().isAfter(LocalDate.now())) {
            return createFailureMessage(message);
        }

        // 프로그램 타입에 따른 정원 감소 처리
        if (program.getType() == ProgramType.TEMPLE_STAY) {
            return reduceTempleStayAvailableCapacity(program, message);
        } else if (program.getType() == ProgramType.BLIND_DATE) {
            return reduceBlindDateAvailableCapacity(program, message);
        }

        return createFailureMessage(message);
    }


    private ReservationConfirmMessage reduceTempleStayAvailableCapacity(Program program, ReservationCreateMessage message) throws JsonProcessingException {
        String templeStayCacheKey = "templeStayInfo:" + message.programId() + "date:" + message.programDate();

        // Redis 캐시에서 TempleStay 정보 조회
        TempleStayDailyInfo templeStayDailyInfo = (TempleStayDailyInfo) redisTemplate.opsForValue().get(templeStayCacheKey);

        // 캐시 데이터가 없으면 DB에서 조회 후 캐싱
        if (templeStayDailyInfo == null) {
            templeStayDailyInfo = jpaTempleStayDailyInfoRepository.findByProgram_IdAndProgramDate(program.getId(), message.programDate())
                    .orElseThrow(() -> new ProgramException(ProgramStatusCode.TEMPLE_STAY_DAILY_INFO_NOT_FOUND));
            redisTemplate.opsForValue().set(templeStayCacheKey, templeStayDailyInfo, Duration.ofHours(1));
            return createFailureMessage(message);
        }

        if (templeStayDailyInfo.getStatus() != ProgramStatus.ACTIVE) {
            createFailureMessage(message);
            return createFailureMessage(message);
        }

        templeStayDailyInfo.reduceAvailableCapacity(message.amount());

        // Redis 캐시에 업데이트
        redisTemplate.opsForValue().set(templeStayCacheKey, templeStayDailyInfo, Duration.ofHours(1));

        return createSuccessMessage(message);
    }

    private ReservationConfirmMessage reduceBlindDateAvailableCapacity(Program program, ReservationCreateMessage message) throws JsonProcessingException {
        String blindDateCacheKey = "blindDateInfo:" + message.programId() + "date:" + message.programDate();

        // Redis 캐시에서 BlindDate 정보 조회
        BlindDateInfo blindDateInfo = (BlindDateInfo) redisTemplate.opsForValue().get(blindDateCacheKey);

        // 캐시 데이터가 없으면 DB에서 조회 후 캐싱
        if (blindDateInfo == null) {
            blindDateInfo = program.getBlindDateInfo();
            if (blindDateInfo == null) {
                return createFailureMessage(message);
            }
            redisTemplate.opsForValue().set(blindDateCacheKey, blindDateInfo, Duration.ofHours(1));
        }

        if (message.gender() == Gender.MALE && blindDateInfo.getGenderStatus() == ProgramGenderStatus.MALE_CLOSED
                || message.gender() == Gender.FEMALE && blindDateInfo.getGenderStatus() == ProgramGenderStatus.FEMALE_CLOSED
                || blindDateInfo.getStatus() != ProgramStatus.ACTIVE) {
            return createFailureMessage(message);
        }

        blindDateInfo.reduceAvailableCapacity(message.gender());

        // Redis 캐시에 업데이트
        redisTemplate.opsForValue().set(blindDateCacheKey, blindDateInfo, Duration.ofHours(1));

        return createSuccessMessage(message);
    }

    private ReservationConfirmMessage createFailureMessage(ReservationCreateMessage message) throws JsonProcessingException {
        String reservationConfirmKey = "reservationId:" + message.reservationId();
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        // 실패 메시지 생성
        ReservationConfirmMessage reservationConfirmMessage = ReservationConfirmMessage.SuccessMessageFrom(message.reservationId());

        // Redis Hash에 데이터와 상태를 저장
        String bodyJson = objectMapper.writeValueAsString(reservationConfirmMessage);
        hashOps.put(reservationConfirmKey, "body", bodyJson); // 메시지 데이터 저장
        hashOps.put(reservationConfirmKey, "status", ReservationConfirmMessageStatus.PENDING); // 상태 저장
        redisTemplate.expire(reservationConfirmKey, Duration.ofHours(1)); // TTL 설정

        return reservationConfirmMessage;
    }

    private ReservationConfirmMessage createSuccessMessage(ReservationCreateMessage message) throws JsonProcessingException {
        String reservationConfirmKey = "reservationId:" + message.reservationId();
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        // 실패 메시지 생성
        ReservationConfirmMessage reservationConfirmMessage = ReservationConfirmMessage.FailureMessageFrom(message.reservationId());

        // Redis Hash에 데이터와 상태를 저장
        String bodyJson = objectMapper.writeValueAsString(reservationConfirmMessage);
        hashOps.put(reservationConfirmKey, "body", bodyJson);; // 메시지 데이터 저장
        hashOps.put(reservationConfirmKey, "status", ReservationConfirmMessageStatus.PENDING); // 상태 저장
        redisTemplate.expire(reservationConfirmKey, Duration.ofHours(1)); // TTL 설정

        return reservationConfirmMessage;
    }
}

