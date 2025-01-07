package com.templlo.service.program.controller;

import com.templlo.service.program.dto.CreateProgramRequest;
import com.templlo.service.program.dto.SimpleProgramResponse;
import com.templlo.service.program.exception.ProgramStatusCode;
import com.templlo.service.program.global.common.response.ApiResponse;
import com.templlo.service.program.service.ProgramService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramService programService;

    @PostMapping
    public ApiResponse<SimpleProgramResponse> createProgram(@Valid @RequestBody CreateProgramRequest request) {
        SimpleProgramResponse response = programService.createProgram(request);
        return ApiResponse.of(ProgramStatusCode.SUCCESS_PROGRAM_CREATE, response);
    }

}
