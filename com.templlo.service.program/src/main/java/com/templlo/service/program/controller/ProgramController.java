package com.templlo.service.program.controller;

import com.templlo.service.program.dto.request.CreateProgramRequest;
import com.templlo.service.program.dto.response.ProgramScheduleResponse;
import com.templlo.service.program.dto.response.ProgramsByTempleResponse;
import com.templlo.service.program.dto.response.SimpleProgramResponse;
import com.templlo.service.program.dto.request.UpdateProgramRequest;
import com.templlo.service.program.entity.ProgramType;
import com.templlo.service.program.exception.ProgramStatusCode;
import com.templlo.service.program.global.common.response.ApiResponse;
import com.templlo.service.program.security.UserDetailsImpl;
import com.templlo.service.program.service.ProgramService;
import com.templlo.service.program.util.PagingUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramService programService;

    @PreAuthorize("hasAnyAuthority('TEMPLE_ADMIN', 'MASTER')")
    @PostMapping
    public ApiResponse<SimpleProgramResponse> createProgram(@Valid @RequestBody CreateProgramRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.of(ProgramStatusCode.SUCCESS_PROGRAM_CREATE, programService.createProgram(request, userDetails));
    }
    // 프로그램 전체 조회
    @GetMapping
    public PagedModel<SimpleProgramResponse> getPrograms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAsc,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ProgramType type,
            @RequestParam(required = false) List<String> programDays) {

        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);
        return programService.getPrograms(keyword, type, programDays, pageable);
    }

    // 프로그램의 스케쥴 상세 조회
    @GetMapping("/{programId}")
    public ApiResponse<ProgramScheduleResponse> getProgramSchedule(@PathVariable(name = "programId") UUID programId, @RequestParam LocalDate programDate) {
        return ApiResponse.of(ProgramStatusCode.SUCCESS_PROGRAM_READ, programService.getProgramSchedule(programId, programDate));
    }

    // 사찰 별 프로그램 조회
    @GetMapping("/temples/{templeId}")
    public ApiResponse<List<ProgramsByTempleResponse>> getProgramsByTemple(@PathVariable(name = "templeId") UUID templeId, @RequestParam(defaultValue = "false") boolean detail) {
        return ApiResponse.of(ProgramStatusCode.SUCCESS_PROGRAM_READ, programService.getProgramsByTemple(templeId, detail));
    }

    @PreAuthorize("hasAnyAuthority('TEMPLE_ADMIN', 'MASTER')")
    @PatchMapping("/{programId}")
    public ApiResponse<SimpleProgramResponse> updateProgram(@PathVariable(name = "programId") UUID programId, @Valid @RequestBody UpdateProgramRequest request,  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.of(ProgramStatusCode.SUCCESS_PROGRAM_UPDATE, programService.updateProgram(programId, request, userDetails));
    }

}
