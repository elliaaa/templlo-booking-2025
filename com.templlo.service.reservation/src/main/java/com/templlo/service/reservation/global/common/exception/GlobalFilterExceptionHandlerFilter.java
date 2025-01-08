package com.templlo.service.reservation.global.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.templlo.service.reservation.global.common.response.ApiResponse;
import com.templlo.service.reservation.global.common.response.StatusCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GlobalFilterExceptionHandlerFilter extends OncePerRequestFilter {
    public final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (BaseException baseException) {
            setErrorResponse(response, baseException.getCode());
        }
    }

    private void setErrorResponse(HttpServletResponse response, StatusCode code) {
        ApiResponse errorResponse = ApiResponse.of(code);
        response.setStatus(code.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //생성한 errorResponse 를 servletResponse 에 write
        try{
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
