package com.templlo.service.program.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.templlo.service.program.entity.*;
import com.templlo.service.program.exception.ProgramException;
import com.templlo.service.program.exception.ProgramStatusCode;
import com.templlo.service.program.global.aop.distributed_lock.DistributedLock;
import com.templlo.service.program.global.aop.distributed_lock.DistributedLockKey;
import com.templlo.service.program.kafka.message.reservation.Gender;
import com.templlo.service.program.kafka.message.reservation.ReservationConfirmMessage;
import com.templlo.service.program.kafka.message.reservation.ReservationCreateMessage;
import com.templlo.service.program.kafka.message.reservation.ReservationStatus;
import com.templlo.service.program.kafka.producer.ReservationConfirmProducer;
import com.templlo.service.program.repository.JpaProgramRepository;
import com.templlo.service.program.repository.JpaTempleStayDailyInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationConsumer {

    private final JpaProgramRepository jpaProgramRepository;
    private final JpaTempleStayDailyInfoRepository jpaTempleStayDailyInfoRepository;
    private final ReservationConfirmProducer reservationConfirmProducer;
//    private final ObjectMapper objectMapper;
//    private final RedissonClient redissonClient;

    @Value("${spring.kafka.topics.reservation-confirmed}")
    private String reservationConfirmedTopic;


    @DistributedLock(keyType = DistributedLockKey.TEMPLE_STAY_PROGRAM_CAPACITY_PREFIX, idSpEL = "#message.programId()", maxWaitTime = 1000L)
    @KafkaListener(topics = "${spring.kafka.topics.reservation-created}", groupId = "reservation-created-program")
    @Transactional
//    public void consumeReservationCreated(String reservationCreatedMessage) throws Exception {
    public void consumeReservationCreated(ReservationCreateMessage message) throws Exception {

        log.info("Consume ReservationCreated Message start");

//        ReservationCreateMessage message = objectMapper.readValue(reservationCreatedMessage, ReservationCreateMessage.class);


        // 프로그램 조회
        Program program = jpaProgramRepository.findById(message.programId()).orElseThrow(
                () -> new ProgramException(ProgramStatusCode.PROGRAM_NOT_FOUND)
        );

//        RLock lock = redissonClient.getLock("programScheduleLock:" + message.programId());
//        lock.lock();  // 락을 먼저 획득

        log.info("Program Schedule Lock acquired for programId: {}", message.programId());

        try {
            // 예약 시작일 전 & 예약 종료일 후 -> 예약 못함 -> 스케쥴러로 오늘날짜 지난 스케쥴들은 전부 INACTIVE 처리
            // 예약 시작일 전 검증 처리
            if (program.getReservationStartDate().isBefore(LocalDate.now())) { // TODO : 테스트해봤는데 이거 로직 반대로 짜신 것 같아요..!
                reservationConfirmProducer.send(reservationConfirmedTopic,
                        ReservationConfirmMessage.from(message.reservationId(), ReservationStatus.FAILURE));
                return;
            }

            // temple stay
            if (program.getType() == ProgramType.TEMPLE_STAY) {

                TempleStayDailyInfo templeStayDailyInfo = jpaTempleStayDailyInfoRepository.findByProgram_IdAndProgramDate(program.getId(), message.programDate())
                        .orElseThrow(() -> new ProgramException(ProgramStatusCode.TEMPLE_STAY_DAILY_INFO_NOT_FOUND));

                // 해당 날짜가 ACTIVE 일 때
                if (templeStayDailyInfo.getStatus() == ProgramStatus.ACTIVE) {
                    // 정원 감소
                    templeStayDailyInfo.reduceAvailableCapacity();
                    log.info("after templeStayDailyInfo.reduceAvailableCapacity()={}", templeStayDailyInfo.getAvailableCapacity());
                    // 예약에 성공 message produce
                    reservationConfirmProducer.send(reservationConfirmedTopic,
                            ReservationConfirmMessage.from(message.reservationId(), ReservationStatus.SUCCESS));

                } else {
                    // 예약에 실패 message produce
                    reservationConfirmProducer.send(reservationConfirmedTopic,
                            ReservationConfirmMessage.from(message.reservationId(), ReservationStatus.FAILURE));
                }
            }

            // blind date
            if (program.getType() == ProgramType.BLIND_DATE) {

                BlindDateInfo blindDateInfo = program.getBlindDateInfo();
                // 정원 마감일 시
                if (blindDateInfo.getStatus() != ProgramStatus.ACTIVE) {
                    reservationConfirmProducer.send(reservationConfirmedTopic,
                            ReservationConfirmMessage.from(message.reservationId(), ReservationStatus.FAILURE));
                    return;
                }
                // 남성 정원 마감인데 남성일 시
                if (message.gender() == Gender.MALE && blindDateInfo.getGenderStatus() == ProgramGenderStatus.MALE_CLOSED) {
                    reservationConfirmProducer.send(reservationConfirmedTopic,
                            ReservationConfirmMessage.from(message.reservationId(), ReservationStatus.FAILURE));
                    return;
                }
                // 여성 정원 마감인데 여성일 시
                if (message.gender() == Gender.FEMALE && blindDateInfo.getGenderStatus() == ProgramGenderStatus.FEMALE_CLOSED) {
                    reservationConfirmProducer.send(reservationConfirmedTopic,
                            ReservationConfirmMessage.from(message.reservationId(), ReservationStatus.FAILURE));
                    return;
                }
                // 정원감소 후 메시지 produce
                blindDateInfo.reduceAvailableCapacity(message.gender());

                reservationConfirmProducer.send(reservationConfirmedTopic,
                        ReservationConfirmMessage.from(message.reservationId(), ReservationStatus.SUCCESS));

            }

        } finally {
            // 락 해제
//            lock.unlock();
//            log.info("Program Schedule Lock released for programId: {}", message.programId());
            log.info("Program Schedule end finally for programId: {}", message.programId());
        }
    }
}