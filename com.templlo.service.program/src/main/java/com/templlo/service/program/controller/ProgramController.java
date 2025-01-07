package com.templlo.service.program.controller;

import com.templlo.service.program.dto.CreateProgramRequest;
import com.templlo.service.program.dto.DetailProgramResponse;
import com.templlo.service.program.dto.SimpleProgramResponse;
import com.templlo.service.program.entity.ProgramType;
import com.templlo.service.program.exception.ProgramStatusCode;
import com.templlo.service.program.global.common.response.ApiResponse;
import com.templlo.service.program.service.ProgramService;
import com.templlo.service.program.util.PagingUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramService programService;

    @PostMapping
    public ApiResponse<SimpleProgramResponse> createProgram(@Valid @RequestBody CreateProgramRequest request) {
        return ApiResponse.of(ProgramStatusCode.SUCCESS_PROGRAM_CREATE, programService.createProgram(request));
    }

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

    @GetMapping("/{programId}")
    public ApiResponse<DetailProgramResponse> getPrograms(@PathVariable UUID programId, @RequestParam LocalDate programDate) {
        return ApiResponse.of(ProgramStatusCode.SUCCESS_PROGRAM_READ, programService.getProgram(programId, programDate));
    }

}
