package com.templlo.service.common.aop;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAspect {

	private final RedissonClient redissonClient;
	private final TransactionTemplate transactionTemplate;

	private final ExpressionParser parser = new SpelExpressionParser();

	@Around("@annotation(distributedLock)")
	public Object handleDistributedLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws
		Throwable {
		// SpEL 파싱 컨텍스트 생성
		StandardEvaluationContext context = new StandardEvaluationContext();
		Object[] args = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++) {
			context.setVariable("arg" + i, args[i]); // 메서드 파라미터를 arg0, arg1 등으로 설정
		}

		// SpEL을 사용해 락 키 동적 생성
		String lockKey = parser.parseExpression(distributedLock.key()).getValue(context, String.class);
		long waitTime = distributedLock.waitTime();
		long leaseTime = distributedLock.leaseTime();

		RLock lock = redissonClient.getLock(lockKey);

		try {
			// 1. 분산락 획득
			if (!lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
				log.warn("락 획득 실패: {}", lockKey);
				throw new IllegalStateException("현재 작업이 진행 중입니다. 잠시 후 다시 시도해주세요.");
			}
			log.debug("락 획득 성공: {}", lockKey);

			// 2. 트랜잭션 시작 및 메서드 실행
			return transactionTemplate.execute(status -> {
				try {
					return joinPoint.proceed();
				} catch (Throwable throwable) {
					throw new RuntimeException(throwable);
				}
			});

		} finally {
			// 3. 락 반납
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
				log.debug("락 해제 성공: {}", lockKey);
			}
		}
	}
}
