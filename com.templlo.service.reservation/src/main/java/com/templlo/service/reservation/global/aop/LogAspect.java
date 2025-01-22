package com.templlo.service.reservation.global.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Slf4j
@Component
public class LogAspect {

    private static final String KEY_REQUEST_PARAMS = "request_params";
    private static final String KEY_DECODE_URI = "decodeURI";
    private static final String KEY_CONTROLLER_CLASS = "controller_class";
    private static final String KEY_CONTROLLER_METHOD = "controller_method";
    private static final String KEY_REQUEST_URI = "requestURI";
    private static final String KEY_HTTP_METHOD = "http_method";

    @Pointcut("execution(* com.templlo.service.reservation..*.*(..)) && !execution(* com.templlo.service.reservation.global..*(..))")
    public void allDomain(){}

    @Pointcut("execution(* com.templlo.service.reservation..*Controller.*(..))")
    public void controller(){}

    @Around("allDomain()")
    public Object aroundAllDomain(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("{} | time : {}ms", joinPoint.getSignature(), endTime - startTime);
        }
    }

    @Around("controller()")
    public Object aroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String controllerClass = joinPoint.getSignature().getDeclaringTypeName();
        String controllerMethod = joinPoint.getSignature().getName();
        Map<String, Object> requestInfos = new HashMap<>();

        requestInfos.put(KEY_CONTROLLER_CLASS, controllerClass);
        requestInfos.put(KEY_CONTROLLER_METHOD, controllerMethod);

        try {
            String decodeURI = URLDecoder.decode(request.getRequestURI(), "UTF-8");
            Map<String, String> params = getParams(request);

            requestInfos.put(KEY_DECODE_URI, decodeURI);
            requestInfos.put(KEY_REQUEST_PARAMS, params);
            requestInfos.put(KEY_REQUEST_URI, decodeURI);
            requestInfos.put(KEY_HTTP_METHOD, request.getMethod());
        } catch (Exception e) {
            log.error("LogAspect.aroundController error", e);
        }

        log.info("[{}] {} : {} - {}", requestInfos.get(KEY_HTTP_METHOD), requestInfos.get(KEY_REQUEST_URI), requestInfos.get(KEY_CONTROLLER_CLASS), requestInfos.get(KEY_CONTROLLER_METHOD));
        log.info("request-params : {}", requestInfos.get(KEY_REQUEST_PARAMS));

        return joinPoint.proceed();
    }

    private Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            String parameterValue = request.getParameter(parameterName);
            params.put(parameterName, parameterValue);
        }
        return params;
    }
}
