package com.templlo.service.program.service;

import com.templlo.service.program.dto.request.CreateProgramRequest;
import com.templlo.service.program.dto.request.UpdateProgramRequest;
import com.templlo.service.program.dto.request.UpdateProgramScheduleRequest;
import com.templlo.service.program.dto.response.*;
import com.templlo.service.program.entity.BlindDateInfo;
import com.templlo.service.program.entity.Program;
import com.templlo.service.program.entity.ProgramType;
import com.templlo.service.program.entity.TempleStayDailyInfo;
import com.templlo.service.program.exception.ProgramException;
import com.templlo.service.program.exception.ProgramStatusCode;
import com.templlo.service.program.feign.TempleClient;
import com.templlo.service.program.global.common.response.BasicStatusCode;
import com.templlo.service.program.repository.JpaProgramRepository;
import com.templlo.service.program.repository.JpaTempleStayDailyInfoRepository;
import com.templlo.service.program.repository.QueryProgramRepository;
import com.templlo.service.program.security.UserDetailsImpl;
import com.templlo.service.program.security.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProgramService {

    private final JpaProgramRepository jpaProgramRepository;
    private final QueryProgramRepository queryProgramRepository;
    private final JpaTempleStayDailyInfoRepository jpaTempleStayDailyInfoRepository;
    private final TempleClient templeClient;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public SimpleProgramResponse createProgram(CreateProgramRequest request, UserDetailsImpl userDetails) {

        log.info("Create program start");

        if (userDetails.getUserRole() == UserRole.TEMPLE_ADMIN) {
            if (!templeClient.checkTempleOwnership(request.templeId()).getStatusCode().is2xxSuccessful()) {
                throw new ProgramException(BasicStatusCode.UNAUTHORIZED);
            }
        }

        // program 생성
        Program program = Program.create(
                request.templeId(),
                request.title(),
                request.description(),
                request.programCapacity() != null ? request.programCapacity() : request.maleCapacity() + request.femaleCapacity(),
                request.type(),
                request.programFee(),
                request.programDays(),
                request.programStartAt(),
                request.reservationStartDate(),
                request.reservationEndDate()
        );

        // type: blind date
        if (request.type() == ProgramType.BLIND_DATE) {

            // add blind_date_info
            program.addBlindDateInfo(BlindDateInfo.create(
                    program,
                    request.programStatus(),
                    request.programDate(),
                    request.maleCapacity(),
                    request.femaleCapacity()

            ));
        }

        // type : temple stay
        if (request.type() == ProgramType.TEMPLE_STAY) {
            // add temple_stay_daily_info
            program.addTempleStayDailInfo(program, request.programStatus(), request.reservationStartDate(), request.reservationEndDate(), request.programDays(), request.programCapacity());
        }

        jpaProgramRepository.save(program);

        log.info("Create program end");

        return SimpleProgramResponse.from(program);

    }

    @Transactional(readOnly = true)
    public PagedModel<SimpleProgramResponse> getPrograms(String keyword, ProgramType type, List<String> days, Pageable pageable) {

        log.info("Get programs start");

        Page<Program> programs = queryProgramRepository.findByKeyword(keyword, type, days, pageable);

        List<SimpleProgramResponse> contents = programs.getContent().stream().map(SimpleProgramResponse::from).toList();

        log.info("Get programs end");

        return new PagedModel<>(new PageImpl<>(contents, pageable, programs.getTotalElements()));

    }


    public ProgramScheduleResponse getProgramSchedule(UUID programId, UUID programScheduleId) {

        log.info("Get program start");

        Program program = jpaProgramRepository.findById(programId).orElseThrow(
                () -> new ProgramException(ProgramStatusCode.PROGRAM_NOT_FOUND)
        );

        if (program.getType() == ProgramType.TEMPLE_STAY) {

            TempleStayDailyInfo templeStayDailyInfo = jpaTempleStayDailyInfoRepository.findById(programScheduleId)
                    .orElseThrow(() -> new ProgramException(ProgramStatusCode.TEMPLE_STAY_DAILY_INFO_NOT_FOUND));

            log.info("Get temple_stay program end");
            return TempleStayScheduleResponse.from(program, templeStayDailyInfo);

        } else {

            BlindDateInfo blindDateInfo = program.getBlindDateInfo();

            if (!program.getBlindDateInfo().getId().equals(programScheduleId)) {
                throw new ProgramException(ProgramStatusCode.BLIND_DATE_INFO_NOT_FOUND);
            }

            log.info("Get blind_date program end");
            return BlindDateScheduleResponse.from(program, blindDateInfo);
        }

    }

    public List<ProgramsByTempleResponse> getProgramsByTemple(UUID templeId, boolean detail) {

        List<Program> programs = jpaProgramRepository.findByTempleId(templeId);

        List<ProgramsByTempleResponse> programsByTempleResponses = new ArrayList<>();

        for (Program program : programs) {
            programsByTempleResponses.add(ProgramsByTempleResponse.From(program, detail));
        }


        return programsByTempleResponses;
    }

    @Transactional
    public SimpleProgramResponse updateProgram(UUID programId, UpdateProgramRequest request, UserDetailsImpl userDetails) {

        log.info("Update program start");

        Program program = jpaProgramRepository.findById(programId).orElseThrow(
                () -> new ProgramException(ProgramStatusCode.PROGRAM_NOT_FOUND)
        );

        if (userDetails.getUserRole() == UserRole.TEMPLE_ADMIN) {
            if (!templeClient.checkTempleOwnership(program.getTempleId()).getStatusCode().is2xxSuccessful()) {
                throw new ProgramException(BasicStatusCode.UNAUTHORIZED);
            }
        }

        program.update(request.title(), request.description(), request.programStartAt());

        log.info("Update program end");

        return SimpleProgramResponse.from(program);

    }

    @Transactional
    public ProgramScheduleResponse updateProgramSchedule(UUID programId, UUID programScheduleId, UpdateProgramScheduleRequest request, UserDetailsImpl userDetails) {
        log.info("Update programSchedule start");

        Program program = jpaProgramRepository.findById(programId).orElseThrow(
                () -> new ProgramException(ProgramStatusCode.PROGRAM_NOT_FOUND)
        );

        if (userDetails.getUserRole() == UserRole.TEMPLE_ADMIN) {
            if (!templeClient.checkTempleOwnership(program.getTempleId()).getStatusCode().is2xxSuccessful()) {
                throw new ProgramException(BasicStatusCode.UNAUTHORIZED);
            }
        }

        if (program.getType() == ProgramType.TEMPLE_STAY) {

            TempleStayDailyInfo templeStayDailyInfo = jpaTempleStayDailyInfoRepository.findById(programScheduleId)
                    .orElseThrow(() -> new ProgramException(ProgramStatusCode.TEMPLE_STAY_DAILY_INFO_NOT_FOUND));

            templeStayDailyInfo.update(request.status());

            log.info("Update programSchedule end");

            return TempleStayScheduleResponse.from(program, templeStayDailyInfo);

        } else {

            BlindDateInfo blindDateInfo = program.getBlindDateInfo();

            if (blindDateInfo == null || !blindDateInfo.getId().equals(programScheduleId)) {
                throw new ProgramException(ProgramStatusCode.BLIND_DATE_INFO_NOT_FOUND);
            }

            if (request.additionalReservationStartDate() != null && request.additionalReservationEndDate() != null) {

                if (!request.additionalReservationStartDate().isBefore(request.additionalReservationEndDate())
                || !request.additionalReservationStartDate().isAfter(program.getReservationEndDate())) {
                    throw new ProgramException(ProgramStatusCode.BAD_REQUEST_BLIND_INFO_ADDITIONAL_RESERVATION_DATE);
                }

            }

            blindDateInfo.update(request.status(), request.additionalReservationStartDate(), request.additionalReservationEndDate());

            log.info("Update programSchedule end");

            return BlindDateScheduleResponse.from(program, blindDateInfo);
        }
    }
}
