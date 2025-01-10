package com.templlo.service.common.client;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.templlo.service.common.dto.DetailProgramResponse;
import com.templlo.service.common.response.ApiResponse;

@FeignClient(name = "program-service", url = "http://localhost:19030")
public interface ProgramFeignClient {

	@GetMapping("/api/programs/{programId}")
	ApiResponse<DetailProgramResponse> getProgram(@PathVariable UUID programId, @RequestParam LocalDate programDate);
}
