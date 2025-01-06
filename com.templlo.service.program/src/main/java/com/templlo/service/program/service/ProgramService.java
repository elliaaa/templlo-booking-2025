package com.templlo.service.program.service;

import com.templlo.service.program.dto.CreateProgramRequest;
import com.templlo.service.program.dto.SimpleProgramResponse;
import com.templlo.service.program.entity.BlindDateInfo;
import com.templlo.service.program.entity.Program;
import com.templlo.service.program.entity.ProgramType;
import com.templlo.service.program.repository.JpaProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProgramService {

    private final JpaProgramRepository jpaProgramRepository;

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

}
