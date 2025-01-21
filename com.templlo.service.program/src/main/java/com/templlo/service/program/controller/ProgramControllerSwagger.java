package com.templlo.service.program.controller;

import com.templlo.service.program.dto.request.CreateProgramRequest;
import com.templlo.service.program.dto.request.UpdateProgramRequest;
import com.templlo.service.program.dto.request.UpdateProgramScheduleRequest;
import com.templlo.service.program.dto.response.ProgramScheduleResponse;
import com.templlo.service.program.dto.response.ProgramsByTempleResponse;
import com.templlo.service.program.dto.response.SimpleProgramResponse;
import com.templlo.service.program.entity.ProgramType;
import com.templlo.service.program.global.common.response.ApiResponse;
import com.templlo.service.program.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Program API", description = "프로그램 관리 API")
public interface ProgramControllerSwagger {

    @Operation(summary = "프로그램 생성", description = "새로운 프로그램을 생성합니다.")
    @PreAuthorize("hasAnyAuthority('TEMPLE_ADMIN', 'MASTER')")
    @PostMapping
    ApiResponse<SimpleProgramResponse> createProgram(
            @Valid @RequestBody CreateProgramRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "프로그램 전체 조회", description = "등록된 모든 프로그램을 조회합니다.")
    @GetMapping
    ApiResponse<PagedModel<SimpleProgramResponse>> getPrograms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAsc,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ProgramType type,
            @RequestParam(required = false) List<String> programDays);

    @Operation(summary = "프로그램 스케줄 조회", description = "특정 프로그램의 스케줄을 조회합니다.")
    @GetMapping("/{programId}/schedules/{programScheduleId}")
    ApiResponse<ProgramScheduleResponse> getProgramSchedule(
            @PathVariable UUID programId,
            @PathVariable UUID programScheduleId);

    @Operation(summary = "사찰별 프로그램 조회", description = "특정 사찰에 등록된 프로그램을 조회합니다.")
    @GetMapping("/temples/{templeId}")
    ApiResponse<List<ProgramsByTempleResponse>> getProgramsByTemple(
            @PathVariable UUID templeId,
            @RequestParam(defaultValue = "false") boolean detail);

    @Operation(summary = "프로그램 업데이트", description = "기존 프로그램의 정보를 업데이트합니다.")
    @PreAuthorize("hasAnyAuthority('TEMPLE_ADMIN', 'MASTER')")
    @PatchMapping("/{programId}")
    ApiResponse<SimpleProgramResponse> updateProgram(
            @PathVariable UUID programId,
            @Valid @RequestBody UpdateProgramRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "프로그램 스케줄 업데이트", description = "특정 프로그램 스케줄을 업데이트합니다.")
    @PreAuthorize("hasAnyAuthority('TEMPLE_ADMIN', 'MASTER')")
    @PatchMapping("/{programId}/schedules/{programScheduleId}")
    ApiResponse<ProgramScheduleResponse> updateProgramSchedule(
            @PathVariable UUID programId,
            @PathVariable UUID programScheduleId,
            @Valid @RequestBody UpdateProgramScheduleRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails);

}
