package com.templlo.service.program.service;

import com.templlo.service.program.dto.*;
import com.templlo.service.program.entity.BlindDateInfo;
import com.templlo.service.program.entity.Program;
import com.templlo.service.program.entity.ProgramType;
import com.templlo.service.program.entity.TempleStayDailyInfo;
import com.templlo.service.program.exception.ProgramException;
import com.templlo.service.program.exception.ProgramStatusCode;
import com.templlo.service.program.repository.JpaProgramRepository;
import com.templlo.service.program.repository.JpaTempleStayDailyInfoRepository;
import com.templlo.service.program.repository.QueryProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProgramService {

    private final JpaProgramRepository jpaProgramRepository;
    private final QueryProgramRepository queryProgramRepository;
    private final JpaTempleStayDailyInfoRepository jpaTempleStayDailyInfoRepository;

    @Transactional
    public SimpleProgramResponse createProgram(CreateProgramRequest request) {

        log.info("Create program start");

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
                    request.programDate(),
                    request.maleCapacity(),
                    request.femaleCapacity()
            ));
        }

        // type : temple stay
        if (request.type() == ProgramType.TEMPLE_STAY) {
            // add temple_stay_daily_info
            program.addTempleStayDailInfo(program, request.reservationStartDate(), request.reservationEndDate(), request.programDays(), request.programCapacity());
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


    public DetailProgramResponse getProgram(UUID programId, LocalDate programDate) {

        log.info("Get program start");

        Program program = jpaProgramRepository.findById(programId).orElseThrow(
                () -> new ProgramException(ProgramStatusCode.PROGRAM_NOT_FOUND)
        );

        if (program.getType() == ProgramType.TEMPLE_STAY) {

            TempleStayDailyInfo templeStayDailyInfo = jpaTempleStayDailyInfoRepository.findByProgram_IdAndProgramDate(programId, programDate)
                    .orElseThrow(() -> new ProgramException(ProgramStatusCode.TEMPLE_STAY_DAILY_INFO_NOT_FOUND));

            log.info("Get temple_stay program end");
            return TempleStayProgramResponse.from(program, templeStayDailyInfo);

        } else {

            if (!program.getBlindDateInfo().getProgramDate().isEqual(programDate)) {
                throw new ProgramException(ProgramStatusCode.BLIND_DATE_INFO_NOT_FOUND);
            }

            log.info("Get blind_date program end");
            return BlindDateProgramResponse.from(program);
        }


    }
}
