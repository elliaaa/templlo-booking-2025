package com.templlo.service.common.aop;

import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

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

	// CouponService의 모든 메서드에 적용
	@Pointcut("execution(* com.templlo.service.coupon.service.CouponService.*(..))")
	public void couponServiceMethods() {
	}

	// PromotionService의 모든 메서드에 적용
	@Pointcut("execution(* com.templlo.service.promotion.service.PromotionService.*(..))")
	public void promotionServiceMethods() {
	}

	@Around("couponServiceMethods() || promotionServiceMethods()")
	public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object result;

		try {
			log.info("[START] {}.{}() 호출", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName());
			result = joinPoint.proceed();
		} catch (Exception e) {
			log.error("[ERROR] {}.{}() 에러: {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), e.getMessage(), e);
			throw e;
		} finally {
			long endTime = System.currentTimeMillis();
			log.info("[END] {}.{}() 실행 시간: {}ms", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), endTime - startTime);
		}

		return result;
	}

	// Controller 레벨 로깅
	@Pointcut("execution(* com.templlo.service..*Controller.*(..))")
	public void controllerMethods() {
	}

	@Around("controllerMethods()")
	public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

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
			log.error("LogAspect.logControllerMethods error", e);
		}

		log.info("[{}] {} : {} - {}", requestInfos.get(KEY_HTTP_METHOD), requestInfos.get(KEY_REQUEST_URI),
			requestInfos.get(KEY_CONTROLLER_CLASS), requestInfos.get(KEY_CONTROLLER_METHOD));
		log.info("Request Params: {}", requestInfos.get(KEY_REQUEST_PARAMS));

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
