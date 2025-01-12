package com.templlo.service.program.global.common.response;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Component
@ControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<ApiResponse<?>> {

    private final HttpServletResponse httpServletResponse;

    public ApiResponseAdvice(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // ApiResponse 타입만 처리
        return ApiResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public ApiResponse<?> beforeBodyWrite(ApiResponse<?> body, MethodParameter returnType, MediaType selectedContentType,
                                          Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                          ServerHttpRequest request, ServerHttpResponse response) {
        // HTTP 상태 코드를 ApiResponse에 설정된 값으로 동기화
        httpServletResponse.setStatus(Integer.parseInt(body.status()));
        return body; // 그대로 반환
    }
}
