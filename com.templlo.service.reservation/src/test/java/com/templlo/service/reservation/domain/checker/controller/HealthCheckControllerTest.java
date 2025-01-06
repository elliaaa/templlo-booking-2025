package com.templlo.service.reservation.domain.checker.controller;

import com.templlo.service.reservation.global.common.response.BasicStatusCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {HealthCheckController.class})
class HealthCheckControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void healthCheck() throws Exception {
        //given
        //when
        //then
        BasicStatusCode statusCode = BasicStatusCode.HEALTH_CHECK_OK;
        mockMvc.perform(get("/api/checker/health-check"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(statusCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(statusCode.getName()))
                .andExpect(jsonPath("$.message").value(statusCode.getMessage()))
                .andDo(print())
                ;
    }
}